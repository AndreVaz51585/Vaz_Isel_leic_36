
class FixedThreadPool(
    nOfThreads: Int,
){
    private val threads : Array<Thread>

    private val lock = ReetrantLock()

    private val workQueue = ArrayDeque<Runnable>(){null}

    private val waitingForWork = lock.newCondition()
    private val waitingForTermination = lock.newCondition()

    private var terminatedThreads = 0

    private var isShutdown = false

    init { require(nOfThreads >= 1)


        threads = Array(nOfThreads){

            thread("Worker-$it"){

                try {
                    while (true){

                        val workItem = takework()
                        if(workItem == null) break
                        workItem.run()

                    }

                } finally {
                lock.withLock{
                    terminatedThreads ++
                    if (terminatedThreads == threads.size) waitingForTermination.signalAll()
                }
                }


            }


        }


    }

    private fun takework() : Runnable? {
        lock.withLock{
            try {

                // we are in shutdown mode and there is no more work to be done, therefore we must return null and signall to all waitingForTermination threads
                if(isShutdown && workQueue.isEmpty()){
                    if(terminatedThreads == nOfThreads)
                    waitingForTermination.signalAll()
                    return null
                }

                // next case is that we are in Shutdown, or not, but the workQueue is not empty, therefore there is work to be done
                if(workQueue.isNotEmpty()){
                    return workQueue.removeFirst()
                }

                // There is no work on the queue  so we must wait for new work, whilist checking for shutdown
                while (true){

                    waitingForWork.await()


                    if(workQueue.isNotEmpty()){
                        return workQueue.removeFirst()
                    }

                    if(isShutdown){
                        waitingForTermination.signalAll()
                        return null
                    }

                }
            } catch (e : Exception){
                throw e
            }

        }
    }



    @Throws(RejectedExecutionException::class)
    fun execute(runnable: Runnable): Unit {
        lock.withLock{

            if(isShutdown) throw RejectedExecutionException

            val toExecute = Runnable {
                runnable.run()
            }

            workQueue.addLast(toExecute)
            waitingForWork.signalAll()
            return

        }
    }





    fun shutdown(): Unit{
    lock.withLock{
        isShutdown = true
        waitingForWork.signalAll()
     }
    }


    @Throws(InterruptedException::class)
    fun awaitTermination(): Unit {
        lock.withLock{

            if (isShutdown && terminatedThreads == threads.size) return
            try {

                while (true){

                    waitingForTermination.await()

                    if(isShutdown && terminatedThreads == threads.size) return


                }


            }   catch (ie : InterruptedException){
                throw ie
            }

        }
    }
}