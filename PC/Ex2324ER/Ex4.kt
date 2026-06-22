
class CyclicCountDownLatch(val initialCount: Int) {


    private data class Request(val cont : CancellableContinuation<Unit>)

    private val requests = LinkedList<Request>()

    private val lock = ReentrantLock()

    init { require(initialCount > 0) }

    private var counter = initialCount


    suspend fun countDown(): Int {
        val toResume : List<Request>

        lock.withLock{

            counter--

            if(counter != 0) return 0

                counter = initialCount

                toResume = requests.toList()
                requests.clear()
        }

        toResume.forEach{it.cont.resume(Unit)}
        return toResume.size

    }

    suspend fun await(): Unit {

     return suspendCancellableCoroutine { cont ->

            val myRequest = Request(cont)

        lock.withLock{
            requests.addLast(myRequest)
        }

            cont.invokeOnCancellation{
                lock.withLock{
                    requests.remove(myRequest)
                }
            }
        }
    }
}