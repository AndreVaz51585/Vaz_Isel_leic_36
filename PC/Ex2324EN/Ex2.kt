
class CyclicCountDownLatch(val initialCount: Int) {

    init { require(initialCount > 0) }

    private val lock = ReetrantLock()

    private val waitingCondition = lock.newCondition()

    private var counter = initialCount

    private data class Request(var isSignaled : boolean = false)

    private val requests = LinkedList<Request>()

    fun countDown(): Int {
        lock.withLock {

            counter--


            // Counter is 0 and there are threads waiting for signal
            if (counter == 0 && requests.isNotEmpty()) {
                counter = initialCount
                val n = requests.size
                requests.forEach{it.isSignaled = true}
                requests.clear()
                waitingCondition.signalAll()
                return n

               // Counter is 0 but no thread is waiting
            } else if (counter == 0 && requests.isEmpty()) {
                counter = initialCount
                return 0
            }
            // counter is not 0 and requests dont matter
            else return 0
        }
    }



    @Throws(InterruptedException::class)
    fun await(timeout: Duration): Boolean {
        lock.withLock{

            val myRequest = Request()
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {
                while (true){

                    remaining = myRequest.condition.awaitNanos(remaining)


                    if(myRequest.isSignaled){
                        return true
                    }

                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return false
                    }

                }


            } catch (ie : InterruptedException ){

                if (myRequest.isSignaled){
                    return true
                }

                requests.remove(myRequest)
                throw ie


            }


        }
    }
}