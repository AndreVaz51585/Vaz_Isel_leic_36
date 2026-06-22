

class MessageSender<T> {

    private val lock = Mutex()

    private data class Request<T>(val cont : Continuation<T>)

    private val requests = LinkedList<Request<T>>()

    suspend fun waitForMessage(): T {
        lock.lock()

        return suspendCoroutine{cont ->
               val myRequest = Request<T>(cont)
                requests.addLast(myRequest)
                lock.unlock()

        }


    }
    suspend fun sendToAll(message: T): Int {
        val toResume : List<Request<T>>

        lock.withLock{

            if(requests.isEmpty()) return 0

            toResume = requests.toList()
            requests.clear()


        }

        toResume.forEach{
            it.cont.resume(message)
        }

        return toResume.size
    }
}