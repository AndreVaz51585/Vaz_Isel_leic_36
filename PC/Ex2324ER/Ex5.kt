

fun <T> toFuture(scope: CoroutineScope, provider: suspend () -> T): CompletableFuture<T> {

    val value : CompletableFuture<T>()


    scope.launch{
        try {
            val result = provider()
            value.complete(result)


        } catch (e : Exception){
            throw  e
        }
    }

    return value

}