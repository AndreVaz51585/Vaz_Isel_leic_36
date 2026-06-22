

class FixedThreadPool(
    nOfThreads: Int ,
){

    private val lock = ReentrantLock()

    private var isShutdown = false

    private val waitingForWork = lock.newCondition()

    private val waitingForTermination = lock.newCondition()

    private val workQueue = LinkedList<Runnable>()

    val threads : Array<Threads>

    var terminated = 0


    init { require(nOfThreads >= 1)


        thread = Array(nOfThreads) {

            thread("Worker-$it"){

                try {

                    while (true) {

                        val workItem = takeWork()
                        if(workItem == null) break
                        workItem.run()


                    }


                } finally {
                    lock.withLock{
                        terminated ++
                            if(terminated == nOfThreads) waitingForTermination.signalAll()
                    }
                }
            }
        }

    }

    private fun takeWork() : Runnable? {
        lock.withLock {

            if (isShutdown && workQueue.isEmpty()) return null

            if (workQueue.isNotEmpty()) {
                val item = workQueue.removeFirst()
                return item
            }

            try {

                while (true) {

                    waitingForWork.await()


                    if (workQueue.isNotEmpty()) {
                        val item = workQueue.removeFirst()
                        return item

                    }

                    // if it had work, it would have been caught, otherwise if shutdown is active no more work will be added to the queue then we return null
                    if (isShutdown) return null


                }


            } catch (e: Exception) {
                throw e
            }

        }

    }

    @Throws(RejectedExecutionException::class)
    fun execute(runnable: Runnable): Unit {
        lock.withLock{


            if(isShutdown) throw RejectedExecutionException()


            val toExecute = Runnable {
                runnable.run()
            }

            workQueue.addLast(toExecute)
            waitingForWork.signalAll()

        }

    }


    fun shutdown(): Unit {

        if(isShutdown) return

        isShutdown = true
        waitingForWork.signalAll()

        if(terminated == nOfThreads) {
            waitingForTermination.signalAll()
        }

        return

    }



    @Throws(InterruptedException::class)
    fun awaitTermination(): Unit {
        lock.withLock{

            if(isShutdown && terminated == nOfThreads) return

            try {

                while (true){


                    waitingForTermination.await()


                    if(isShutdown && terminated == nOfThreads) return

                }



            } catch (ie : InterruptedException ) {
                throw ie
            }







        }

    }
}
