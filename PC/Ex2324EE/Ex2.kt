import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.time.Duration

class ComputationFuture<T>(val retries: Int, val provider: () -> T) {

    private val lock = ReentrantLock()


    private var value : T? = null

    private var exception : Exception? = null

    private var retriesCounter = retries

    private var currentState : state = state.RUNNING

    private val waitingFor = lock.newCondition()

    private enum class state {
        RUNNING,
        FAILED,
        SUCCEEDED
    }

    init {

        Thread.ofVirtual().start{



            while (retriesCounter >= 0){

                try {

                    val result = provider()

                    lock.withLock{
                        value = result
                        currentState = state.SUCCEEDED
                        waitingFor.signalAll()
                        break
                    }




                } catch (e : Exception){

                    lock.withLock{
                        retriesCounter--
                    }

                    if(retriesCounter == 0){
                        lock.withLock{
                            currentState = state.FAILED
                            exception = e
                            waitingFor.signalAll()
                            break
                        }


                    }



                }
            }
        }


    }



    @Throws(ExecutionException::class, TimeoutException::class,InterruptedException::class)
    fun get(timeout: Duration): T {
        lock.withLock{

            when(currentState){

                state.SUCCEEDED -> return value

                state.FAILED -> throw ExecutionException(exception)

                state.RUNNING -> {

                    var remaining = timeout.toNanos()

                    try {
                        while (true){

                            remaining = waitingFor.awaitNanos(remaining)

                            if(currentState == state.SUCCEEDED) return value

                            if( currentState == state.FAILED) throw ExecutionException(exception)

                            if(remaining <= 0){
                                throw TimeoutException()
                            }
                        }



                    } catch (ie : InterruptedException){
                        if( currentState == state.SUCCEEDED) return value as T
                        else if (currentState == state.FAILED) throw ExecutionException(exception)
                        else throw ie


                    }





                }



            }




        }

    }
}