suspend fun <T> firstNOrCancel(
    n: Int,
    futures: List<CompletableFuture<T>>
): List<T> {

    return suspendCoroutine {cont ->

    val list = mutableListOf<T>()
    var isDone = false

    var exceptions = 0
    var completed = 0

    val lock = ReentrantLock()

    var lastException : Throwable? = null

        for (future in futures) {


            future.whenComplete{value, exception ->
                lock.withLock {
                    if (isDone){
                    futures.forEach{
                        it.cancel(true)
                    }
                    return@whenComplete

                    }

                    if (exception != null) {
                        exceptions++
                        lastException = exception

                        if (futures.size - exceptions < n) { // o numero de sucessos já não pode ser atingido
                            isDone = true
                            cont.resumeWithException(lastException)

                        }

                    } else {
                        if (isDone){
                            futures.forEach{
                                it.cancel(true)
                            }
                            return@whenComplete
                        }

                        completed ++
                        list.add(value)
                        if(completed == n){
                            isDone = true
                            cont.resume(list)
                        }
                    }
                }

            }




        }




    }




}
