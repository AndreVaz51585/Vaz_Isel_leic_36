

class MessageQueue<T>() {

    private val lock = ReentrantLock()

    private data class EnqueueRequest<T>(val message : T , val cond : Condition , var signaledBy : Thread? = null)

    private data class DequeueRequest<T>(var message : T? = null , val cond : Condition, var isSignaled : Boolean = false )

    private val enqueueRequests = LinkedList<EnqueueRequest<T>>()

    private val dequeueRequests = LinkedList<DequeueRequest<T>>()



    @Throws(InterruptedException::class)
    fun tryEnqueue(message: T, timeout: Duration): Thread? {
        lock.withLock{




        }
    }


    @Throws(InterruptedException::class)
    fun tryDequeue(nOfMessages: Int, timeout: Duration): List<T> {
        lock.withLock{

            // no previous requests
            if(dequeueRequests.isEmpty()){
                // lets check if we have enough messages
                if(enqueueRequests.size >= nOfMessages){
                    // lets just consume
                    var counter = 0
                    val messages = mutableListOf<T>()
                    while (counter < nOfMessages){
                        val first = enqueueRequests.removeFirst()
                        messages.add(first.message)
                        first.signaledBy = Thread.currentThread()
                        first.cond.signal()
                        counter++
                    }
                    return messages
                }

                // if we are first request but there isnt enough messages for us we create a requeest and wait for timeout to consume whats available

                else {
                    val myRequest = DequeueRequest<T>(lock.newCondition())
                    dequeueRequests.addLast(myRequest)

                    var remaining = timeout.toNanos()

                    try {

                        while (true){

                            remaining = myRequest.awaitNanos(remaining)






                        }


                    } catch (ie : InterruptedException){


                    }

                }




            }




            val myRequest = DequeueRequest<T>(lock.newCondition(), Thread.currentThread())
            dequeueRequests.addLast(myRequest)
        }
    }
}