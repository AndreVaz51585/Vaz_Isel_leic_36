
class MessageSender<T> {

    private val lock = Mutex()

    private data class Request<T>(val cont : Continuation<T>)

    private val requests = LinkedList<Request<T>>()


    suspend fun waitForMessage(): T {
        lock.lock()
            return suspendCourotine { cont ->
                val myRequest = Request<T>(cont)
                requests.addLast(myRequest)
                lock.unlock()

        }
    }


    suspend fun sendToOne(message: T): Boolean {
        lock.withLock{

            if(requests.isEmpty()) return false

            val first = requests.removeFirst()
            first.cont.resume(message)
            return true


        }
    }
}


