class UnboundedMessageQueue<T> {

    private val lock = ReentrantLock()

    private data class Request<T>(val condition = lock.newCondition(), var message : T? = null)

    private val queue = LinkedList<T>()

    private val requests = LinkedList<Request<T>>()

    private var isShuttingDown = false

    private val waitingForShutDown = lock.newCondition()


    @Throws(RejectedExecutionException::class)
    fun enqueue(value: T): Unit {
        lock.withLock {


            if (isShuttingDown) throw RejectedExecutionException

            // Fast Path: there is no requests waiting for message

            if (requests.isEmpty()) {
                queue.addLast(value)
                return
            }

            // Slow Path, there are requests for messages
            else {
                val request = requests.removeFirst()
                request.message = value
                request.condition.signal()
                return
            }

        }
    }

    @Throws(InterruptedException::class)
    fun tryDequeue(timeout: Duration): T? {
        lock.withLock {


            // If shuttdown and there is no more messages we must signal the waiting termination threads
            if (isShuttingDown && queue.isEmpty()) {
                waitingForShutDown.signalAll()
                return null

            }

            if (queue.isNotEmpty()) {
                val value: T = queue.removeFirst()
                if (isShuttingDown && queue.isEmpty()) {
                    waitingForShutDown.signalAll()
                }
                return value

            }

            val myRequest = Request<T>()
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {
                while (true) {

                    remaining = myRequest.condition.awaitNanos(remaining)

                    if (myRequest.message != null) {
                        return myRequest.message
                    }

                    // verificar se a queue is empty é inutil porque, uma vez nesta parte do ciclo, onde viemos porque não havia valores na queue, uma vez que estamos em shutdown os producers já não nos conseguem entregar nada, nem a nós nem à fila.
                    if (isShuttingDown) {
                        requests.remove(myRequest)
                        waitingForShutDown.signalAll()
                        return null
                    }

                    if (remaining <= 0) {
                        requests.remove(myRequest)
                        return null
                    }

                }


            } catch (ie: InterruptedException) {
                if (myRequest.message != null) {
                    return myRequest.message
                } else {
                    requests.remove(myRequest)
                    throw ie
                }


            }
        }
    }

    @Throws(InterruptedException::class)
    fun closeAndWaitForEmpty(timeout: Duration): Boolean {
        lock.withLock{
            isShuttingDown = true
            requests.forEach{it.condition.signal()}
            // FastPath, queue is already empty so we can close.
            if(queue.isEmpty()) return true

            var remaining = timeout.toNanos()

            try {
                while (true){

                    remaining = waitingForShutDown.awaitNanos(remaining)

                    if(queue.isEmpty()){
                        return true
                    }

                    if (remaining <=0){
                        return false
                    }

                }


            } catch (ie : InterruptedException ){
                throw ie

            }


        }
    }
}