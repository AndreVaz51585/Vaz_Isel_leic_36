
class ThreadPool(
    nOfThreads: Int
) {

      private val lock = ReentrantLock()

      private var isShutDown = false

      private val work = ArrayDeque<Runnable>()

      private val waitingForWork = lock.newCondition()

      private val terminationCondition = lock.newCondition()

      private val threads : Array<Thread>

    init {
        require(nOfThreads > 0)

        threads = Array(size = nOfThreads) {

            thread(name = "Worker-$it") {

                while (true) {

                    try {

                        val workItem = takeWork()
                        if (workItem == null) break
                            workItem.run()



                    } catch (ie: InterruptedException) {
                        throw ie
                        break

                    }


                }


            }
        }

    }


}

}



    private fun takeWork(): Runnable? {
        lock.withLock {

        // Fast path, there is work to be done

            if (isShutDown && work.isEmpty()) {
                return null
            }

          if( work.isNotEmpty()){
              return work.removeFirst()
          }

          // Slow path, there is no work to be done so we must wait

          while (true){

           waitingForCondition.await()

           if (isShutdown){
               return null
           }

           if( work.isNotEmpty()){
               return work.removeFirst()
           }
          }
        }
    }



    @Throws(RejectedExecutionException::class)
    fun execute(runnable: Runnable): Unit {
        lock.withLock{

        if (isShutDown){
            throw RejectedExecutionException
        }

        val job = Runnable{
          runnable.run()
        }

        work.addLast(job)
        waitingForWork.signalAll()


        }
    }


    @Throws(InterruptedException::class)
    fun shutdownAndWaitForTermination(): Unit {
        lock.withLock{
         try {
             isShutDown = true
             waitingForWork = condition.signalAll()
         } catch (ie : InterruptedException){
             throw ie
         }

        }
        threads.forEach{it.join()}
    }
}
