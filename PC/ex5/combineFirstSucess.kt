

suspend fun <A, B, R> combineFirstSuccessful(
    futuresA: List<CompletableFuture<A>>,
    futuresB: List<CompletableFuture<B>>,
    combiner: (A, B) -> R
): R {

    val lock = ReentrantLock()


    return suspendCoroutine {cont ->

        var exceptionsA = 0
        var exceptionsB = 0

        var done = false


        var valueA : A? = null
        var valueB : B? = null

        var lastException : Throwable? = null

        for (future in futuresA){future ->
            future.whenComplete{value, exception ->
                if(exception != null){
                    lock.withLock{

                        if(done) return

                        lastException = exception
                        exceptionsA ++
                        if(exceptionsA == futuresA.size && !done){

                            done = true
                            cont.resumeWithException(exception)
                        }
                    }

                else {
                       lock.withLock{
                           if(done) return

                           valueA = value as A // ou seja, se não existia valor atribuimos e agora só temos de esperar
                           if(valueB != null){
                                done = true
                               cont.resume(combiner(valueA,valueB))
                           }
                           }
                       }



                    }
                }



            }
        for (future in futuresB){future ->
            future.whenComplete{value, exception ->
                if(exception != null){
                    lock.withLock{
                        if(done) return

                        lastException = exception
                        exceptionsB ++
                        if(exceptionsB == futuresB.size && !done) {

                            done = true
                            cont.resumeWithException(exception)
                        }
                    }

                    else {
                        lock.withLock{
                            valueB = value as B // ou seja, se não existia valor atribuimos e agora só temos de esperar
                            if(valueA != null){
                                done = true
                                cont.resume(combiner(valueA,valueB))
                            }
                        }
                    }



                }
            }



        }


        }


        for (future in futuresB){future ->




        }



    }





}

