

SynchronousQueue<T> {


    private val guard = Mutex()

    private val enqueueRequests = LinkedList<enQueueRequest<T>>()

    private val dequeueRequests = LinkedList<deQueueRequest<T>>()

    private data class enQueueRequest<T>(val continuation : Continuation, val message : T? = null)

    private data class deQueueRequest<T>(val continuation : Continuation)

    suspend fun enqueue(message: T): Unit{
        guard.withLock{

            // There is a pending dequeue request so we must resume it to check for values

            if(dequeueRequests.isNotEmpty()){
                val first = dequeueRequests.removeFirst()
                first.continuation.resume(message)
            }

            // OtherWise we just add the enqueue request and suspend

           return suspendCourotine{ cont ->
                val myRequest = Request(cont,message)
                enqueueRequests.addLast(myRequest)
            }
        }
    }
}

    suspend fun dequeue(): T {
        guard.lock()

        if(enqueRequests.isNotEmpty()){
            val request = enQueueRequests.removeFirst()
            request.continuation.resume(Unit)
            return request.message

        return suspendCourotine{cont ->
            dequeueRequests.addLast(DequeueRequest(cont))
            guard.unlock()
        }

    }

}