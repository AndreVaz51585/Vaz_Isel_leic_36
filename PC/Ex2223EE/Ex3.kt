

class Exchanger<T> {

    private val lock = ReentrantLock()

    private data class Request<T>(val condition : Condition, val value : T, var toReceive : T? = null, var delivered : Boolean = false)

    private val requests = LinkedList<Request<T>>()


    @Throws(InterruptedException::class)
    fun exchange(value: T, timeout: Duration): T? {
        lock.withLock{

            if(requests.isNotEmpty()){

                val first = requests.removeFirst()
                first.toReceive = value
                first.delivered = true
                first.condition.signal()
                return first.value
            }

            val myRequest = Request<T>(lock.newCondition(),value)
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {

                while (true){

                    remaining = myRequest.condition.awaitNanos(remaining)

                    if(myRequest.delivered) return myRequest.toReceive

                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return null

                    }

                }


            } catch (ie : InterruptedException ) {

                if(myRequest.delivered) return myRequest.toReceive
                else {
                    requests.remove(myRequest)
                    throw ie
                }


            }

        }
    }
}