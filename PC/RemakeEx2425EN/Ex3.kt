class CyclicCountDownLatch(val initialCount: Int) {


    private var counter = initialCount

    private val lock = Mutex()

    private data class Request(val cont : Continuation<Unit>)

    private val requests = LinkedList<Request>()



    init { require(initialCount > 0) }


   suspend fun countDownAndAwait(): Unit {
        var toResume : List<Request>? = null


        lock.lock()

            counter --

                if(counter == 0){

                        counter = initialCount

                            toResume = requests.toList()
                            requests.clear()


                            lock.unlock()
                            toResume.forEach{it.cont.resume(Unit)}

                            return
                        }


                }


            return suspendCoroutine { cont ->

                val myRequest = Request()
                requests.addLast(myRequest)
                lock.unlock()

            }



        }


    }
}