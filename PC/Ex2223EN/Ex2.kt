class MessageBroadcaster<T> {

    private val requests = LinkedList<Request<T>>()

    private val lock = ReetrantLock()
    private data class Request<T>(var thread : Thread , var message : T? = null, val condition : Condition = lock.condition())


    @Throws(InterruptedException::class)
    fun waitForMessage(timeout: Duration): T? {

        lock.withLock {
            // There is no fast path

            val myRequest = Request<T>(Thread.currrentThread())
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {
                while (true) {

                    remaing = myRequest.condition.awaitNanos(remaining)

                    if (myRequest.message != null) {
                        return myRequest.message
                    }

                    if (remaining <= 0) {
                        requests.remove(myRequest)
                        return null
                    }

                }

            } catch (ie: InterruptedException) {
                requests.remove(myRequest)
                throw ie

            }
        }
    }

    fun sendToAll(message: T): List<Thread> {
        lock.withLock{

            // Fast path: there is no request, so we can just return an empty list
            if (requests.isEmpty()){
                return emptyList()
            }

            // Slow Path, we must empty the requests list
            val threadList = mutableListOf<Thread>()

            while (requests.isNotEmtpy()){
               val first = requests.removeFirst()
                first.message = message
                threadList.add(first.thread)
                first.condition.signal()
            }

            return threadList

        }

    }
}