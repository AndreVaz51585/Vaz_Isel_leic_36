suspend fun <T, U, R> combineFutures(f1: CompletableFuture<T>, f2: CompletableFuture<U>, combiner: (T,U)->R): R {

    val value1 = f1.awaitNonBlocking()
    val value2 = f2.awaitNonBlocking()

    return combiner(value1,value2)


}


suspend fun <T> CompletableFuture<T>.awaitNonBlocking() : T =
        suspendCourotine { cont ->
            this.whenComplete { value, exception ->
                if(exception != null){
                    cont.resumeWithException(exception)
                }
                else{
                    cont.resume(value)
                }
            }
        }


