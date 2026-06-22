class CyclicCountDownLatch(val initialCount: Int) {


    private var counter = initialCount

    private val lock = ReentrantLock()

    private val waitingCondition = lock.newCondition()

    private data class Request(var isSignaled : Boolean = false)

    private val requests = LinkedList<Request>()



    init { require(initialCount > 0) }


    @Throws(InterruptedException::class)
    fun countDownAndAwait(timeout: Duration): Boolean {
        lock.withLock{

            counter --

            if(counter == 0){

                counter = initialCount

                if(requests.isNotEmpty()){
                    requests.forEach{it.isSignaled = true}
                    requests.clear()
                    waitingCondition.signalAll()
                }

                return true
            }

            val myRequest = Request()
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {
                while (true){

                    remaining = waitingCondition.awaitNanos(remaining)

                    if(myRequest.isSignaled) return true

                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return false
                    }


                }

            } catch (ie :InterruptedException ) {
                if(myRequest.isSignaled) return true
                else {
                    requests.remove(myRequest)
                    throw ie
                }

            }


        }


    }
}