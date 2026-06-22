
class Semaphore(private val initialUnits: Int) {

    var totalUnit = initialUnits
    var isShutDown = false

    val lock = ReentrantLock()
    private val requests = LinkedList<Request>()

    val terminationCondition = lock.newCondition()


    private data class Request(var signaled : Boolean = false, val condition : Condition = lock.newCondition())


    fun release(): Unit {
        lock.withLock{

            //Fast Path : There is no requests
            if(requests.isEmpty()){
                totalUnit++
                if(isShutDown && totalUnit == initialUnits){
                    terminationCondition.signalAll()
                }
                return

            }



            // Slow path, we must signal to the thread
            val firstRequest = requests.removeFirst()
            firstRequest.signaled = true
            firstRequest.condition.signal()
            return


        }
    }


    @Throws(InterruptedException::class, RejectedExecutionException::class)
    fun acquire(timeout: Duration): Boolean {
        lock.withLock{

            // if its close we must throw the exception
            if (isShutDown){
                throw RejectedExecutionException()
            }

            //Fast path, there is units to be read. The requests here dont matter because if there is units is because there is no request and the release deliveers directly to the request without incremeting
            if (totalUnit > 0){
                totalUnit--
                return true
            }

            // Slow Path: there is no units to be consumed, we need to create a request

            val myRequest = Request()
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

           try {
               while (true){

                   remaining = myRequest.condition.awaitNanos(remaining)

                   if(myRequest.signaled){
                       return true
                   }


                   if(isShutDown){
                       throw RejectedExecutionException()
                   }



                   if(remaining <= 0){
                       requests.remove(myRequest)
                       return false
                   }

               }


           } catch (ie : InterruptedException){
               if(myRequest.signaled){
                   return true
               }
               else {
                   requests.remove(myRequest)
               }
               throw ie

           }


        }
    }

    fun shutdown(): Unit {
        lock.withLock{

            isShutDown = true

            while (requests.isNotEmpty()){
                val firstRequest = requests.removeFirst()
                firstRequest.condition.signal()
            }

             if(totalUnit == initialUnits){
                terminationCondition.signalAll()
             }

        }
    }

    @Throws(InterruptedException::class)
    fun awaitTermination(timeout: Duration): Boolean {

        lock.withLock{

        // Fast Path: is shutdown and units == InitialUnits

        if (isShutDown && totalUnit == initialUnits){
            return true
        }

        var remaining = timeout.toNanos()

        try {
            while (true){

                remaining = terminationCondition.awaitNanos(remaining)

                if (isShutDown && totalUnit == initialUnits){
                    return true
                }

                if(remaining <= 0){
                    return false
                }


            }

        } catch (ie: InterruptedException){
            throw ie
        }
    }
}
