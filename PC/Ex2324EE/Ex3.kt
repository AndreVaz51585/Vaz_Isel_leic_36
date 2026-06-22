import java.time.Duration
import java.util.LinkedList
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class SynchronousMessageQueue<T> {

    private val lock = ReentrantLock()

    private var isClose = false

    private data class Request<T>(var message : T? = null, var isSignaled : Boolean = false, val cond : Condition, val thread : Thread)

    private val requests = LinkedList<Request<T>>()


    @Throws(RejectedExecutionException::class)
    fun trySend(value: T): Thread? {
        lock.withLock{

            if(isClose) throw RejectedExecutionException()

            if(requests.isEmpty()) return null

            val first = requests.removeFirst()
            first.message = value
            first.isSignaled = true
            first.cond.signal()

            return first.thread


        }

    }

    @Throws(InterruptedException::class)
    fun tryReceive(timeout: Duration): T? {

        lock.withLock{


            if(isClose) return null

            val myRequest = Request<T>(cond = lock.newCondition(), thread =  Thread.currentThread())
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {

                while (true){

                    remaining = myRequest.cond.awaitNanos(remaining)

                    if(myRequest.isSignaled) return myRequest.message

                    if(isClose) return null

                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return null
                    }

                }


            } catch (ie : InterruptedException) {
                if(myRequest.isSignaled) return myRequest.message
                else {
                    requests.remove(myRequest)
                    throw ie
                }


            }



        }

    }

    fun close(): Unit {
        lock.withLock{
            if(isClose) return

            isClose = true

            requests.forEach{
                it.cond.signal()
            }
            requests.clear()
        }

    }
}