
class MessageQueue<T>() {

    private val queue = LinkedList<T>()
    private val requests = LinkedList<Request>()


    data class Request(val nOfMessages : Int, var messageList : List<T>? = null, val condition : Condition = lock.condition())

    private val lock = ReentrantLock()


    fun enqueue(message: T): Unit {
        lock.withLock{

            // Fast Path, there is no request
            if (requests.isEmpty()){

                queue.addlast(message)
                return
            }

            val firstRequest = requests.peekfirst()
            queue.addLast(message)

            if (queue.size >= firstRequest.nOfMessages) {
                val list = mutabListOf<T>()
                var counter = 0

                while (counter < firstRequest.nOfMessages){
                    val first = queue.removeFirst()
                    list.add(first)
                    counter ++
                }
                firstRequest.messageList = list
                requests.remove(firstRequest)
                firstRequest.condition.signal()
                return
            }


        }
    }


    @Throws(InterruptedException::class)
    fun tryDequeue(nOfMessages: Int, timeout: Duration): List<T>? {
        lock.withLock{

            // Fast path, existem messages na Queue e vou consumir até não haver mais.

            if( request.isEmpty() && queue.size >= nOfMessages){
                val list = mutableListOf<T>()
                var counter = 0

                while counter < nOfMessages {
                   val first = queue.peekfirst()
                    list.add(first)
                    queue.removefirst()
                    counter++
                }
                return list
            }

            // Slow Path, there is not enough messages so we create a request

            val myrequest = Request(nOfMessages)
            requests.addLast(myrequest)

            var remainingTime = timeout.toNanos()

            try {

                while (true){

                    remainingTime = myrequest.condition.awaitNanos(remainingTime)

                    if(myrequest.messageList.size != null){
                        return myrequest.messageList
                    }

                    if(remainingTime <= 0){
                        requests.remove(myrequest)
                        return null
                    }

                }



            } catch (ie: InterruptedException){
                requests.remove(myrequest)
                throw e
            }


        }


    }


}
