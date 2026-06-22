
class CyclicCountDownLatch(val initialCount: Int) {

    init { require(initialCount > 0) }

    private var counter = initialCount


    private val lock = ReentrantLock()

    private val waiting = lock.newCondition()

    private data class Request(var isSignaled : Boolean = false)

    private val requests = LinkedList<Request>()



    @Throws(InterruptedException::class)
    fun countDownAndAwait(timeout: Duration): Boolean{
        lock.withLock{

            counter--

            if(counter == 0){
                  counter = initialCount


                  if(requests.isNotEmpty()){
                      requests.forEach{it.isSignaled = true}
                      waiting.signalAll()
                      requests.clear()
                  }
                return true
            }

            val myRequest = Request()
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {

                while (true){

                    remaining = waiting.awaitNanos(remaining)

                    if(myRequest.isSignaled) return true


                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return false
                    }
                }


            }catch (ie : InterruptedException){
                if(myRequest.isSignaled) return true
                else {
                    requests.remove(myRequest)
                    throw  ie
                }
            }


        }

    }


}
