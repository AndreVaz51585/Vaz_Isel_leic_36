

suspend fun <T> await(future: CompletableFuture<T>): T {

    return suspendCoroutine {cont ->

        future.whenComplete{value, exception ->
            if(exception != null) cont.resumeWithException(exception)
            else{
                cont.resume(value)
            }

        }
    }
}