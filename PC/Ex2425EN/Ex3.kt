
class CyclicCountDownLatch(val initialCount: Int) {

    init { require(initialCount > 0) }

    private var counter = initialCount

    private val lock = Mutex()

    private data class Request(val cont : Continuation<Unit>)

    private val requests = LinkedList<Request>()

    suspend fun countDownAndAwait(): Unit {
        lock.lock()

        counter --

        if(counter == 0){

            counter = initialCount

            if(requests.isNotEmpty()){
                requests.forEach{it.cont.resume(Unit)}
                requests.clear()
            }

            // val toResume = requests.toList()
            //requests.clear()
            // lock.unlock()
            // toResume.forEach{it.cont.resume(Unit)}


            lock.unlock()
            return
        }

        return suspendCoroutine{ cont ->
              val myRequest = Request(cont)
               requests.addLast(myRequest)
               lock.unlock()

        }
    }
}