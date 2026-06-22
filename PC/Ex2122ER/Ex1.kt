
class ExecutorWithShutdown(private val executor: Executor) {

    private val lock = ReentrantLock()

    private var remaining = 0

    private val waitingForTermination = lock.newCondition()

    private var isShutdown = false

    @Throws(RejectedExecutionException::class)
    fun execute(command: Runnable): Unit {
         lock.withLock{

             if(isShutdown) throw RejectedExecutionException()

             remaining ++

             val toExecute = Runnable {
                 try {
                     command.run()
                 } finally {
                     lock.withLock{
                     remaining --
                     if(remaining == 0 && isShutdown) waitingForTermination.signalAll()
                     }
                 }

              }

            return executor.execute(toExecute)
         }

    }


    fun shutdown(): Unit {
        lock.withLock{
            isShutdown = true
                if(remaining == 0) waitingForTermination.signalAll()
            return
        }
    }


    @Throws(InterruptedException::class)
    fun awaitTermination(timeout: Duration): Boolean {
        lock.withLock{
            if(isShutdown && remaining == 0) return true

            var remainingTime = timeout.toNanos()

            try {
                while (true){

                    remainingTime = waitingForTermination.awaitNanos(remainingTime)

                    if(isShutdown && remaining == 0) return true

                    if(remaining <= 0) return false


                }

            } catch (ie : InterruptedException){
                    throw ie
            }
        }
    }
}