class WaitQueue {

    private val lock = ReentrantLock()

    private data class Request(val condition : Condition, val thread : Thread, var isSignaled : Boolean = false)


    private val requests = LinkedList<Request>()

    @Throws(InterruptedException::class)
    fun await(timeout: Duration): Boolean {
        lock.withLock{

            var remaining = timeout.toNanos()
            val myRequest = Request(lock.newCondition(), Thread.currentThread())
            requests.addLast(myRequest)


            try {

                while(true){

                    remaining = myRequest.condition.awaitNanos(remaining)

                    if(myRequest.isSignaled) return true


                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return false

                    }
                }


            } catch (ie : InterruptedException ){
                if(myRequest.isSignaled) return true

                else {
                    requests.remove(myRequest)
                    throw ie
                }
            }



        }
    }


    fun signalOne(): Thread? {
        lock.withLock{
            // Fast path, there is no requests we just return null

            if(requests.isEmpty()) return null

            else {
                val first = requests.removeFirst()
                first.isSignaled = true
                first.condition.signal()
                return first.thread
            }
        }
    }
}