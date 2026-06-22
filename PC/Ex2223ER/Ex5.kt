
suspend fun <T> anyN(n: Int, futures: List<CompletableFuture<T>>): List<T> {

    return suspendCoroutine { cont ->

        val list = mutableListOf<T>()
        var completed = 0
        var done = false
        var lastException : Throwable? = null

         val lock = ReentrantLock()

        futures.forEach{ future ->
            future.whenComplete {value, exception ->

                lock.withLock{
                    if (done) return

                    completed ++

                    if (exception == null) {
                        list.add(value)

                        if(list.size == n) {
                            done = true
                            cont.resume(list.toList())
                        }

                    }
                    else {

                        lastException = exception

                        val remaining = futures.size - completed

                        val possibleSuccesses = results.size + remaining

                        if (possibleSuccesses < n) {

                            done = true

                            cont.resumeWithException(lastException!!)

                        }


                }

            }


        }

    }



}
