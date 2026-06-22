


class Exchanger<T> {

    private val continuations = ArrayDeque<Request<T>>()

    private data class Request<T>(val value : T, val continuation : Continuation<T>)

    private val lock = Mutex()

    suspend fun exchange(value: T): T {
        lock.lock()

        // Fast Path: continuations is empty, no value to exchange, so we suspend and add the continuation to the queue.
        if (continuations.isEmpty()) {
            return suspendCourotine { cont ->
                val myRequest = Request(value, cont)
                continuations.addLast(myRequest)
                lock.unlock()
            }
        }
            // Slow path, there is continuations waiting, so we can exchange the value and resume the first continuation in the queue.

            else{
                val partner = continuations.removeFirst()
                lock.unlock()

                partner.continuation.resume(value)
                return partner.value
            }
        }
    }
}