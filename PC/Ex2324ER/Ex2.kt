




class ComputationFuture<T>(val provider: () -> T) {

   private enum class state {
        RUNNING,
        FAILED,
        SUCCEDED
    }

    private val lock = ReentrantLock()

    private var currentState : state

    private var result : T? = null

    private var exception : Exception? = null

    private val waitingCondition = lock.newCondition()


    init {
        currentState = state.RUNNING


        Thread.ofVirtual().start{


          try {
              val currentResult =  provider()

              lock.withLock{
                  result = currentResult
                  currentState = state.SUCCEDED
                  waitingCondition.signalAll()
              }

          } catch (e : Exception){
                lock.withLock{
                    exception = e
                    currentState = state.FAILED
                    waitingCondition.signalAll()

                }
          }
        }
    }



    @Throws(ExecutionException::class,TimeoutException::class,InterruptedException::class)
    fun get(timeout: Duration): T {
        lock.withLock{

            when(currentState) {

                 state.SUCCEDED -> {
                     return result as T
                }

                 state.FAILED -> {
                     throw ExecutionException(exception) // ou ExecutionException
                }


                 state.RUNNING -> {

                    var remaining = timeout.toNanos()

                 try {
                     while (true){

                        remaining = waitingCondition.awaitNanos(remaining)

                         if(currentState == state.SUCCEDED){
                             return result as T
                         }

                         if(currentState == state.FAILED){
                             throw ExecutionException(exception)
                         }

                         if(remaining <= 0){
                             throw TimeoutException()
                         }


                     }
                 }
                 catch (ie : InterruptedException){
                        throw ie
                 }



                }


            }

        }
    }
}