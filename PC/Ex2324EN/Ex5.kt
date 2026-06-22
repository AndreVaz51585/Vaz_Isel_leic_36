

suspend fun <T> either(f1: CompletableFuture<T>, f2: CompletableFuture<T>): T {

    val result = select<T> {


        f1.whenComplete{ value, exception ->
            return if(exception != null) exception else value

        }

        f2.whenComplete{ value, exception ->
            return if(exception != null) exception else value

        }
    }





}