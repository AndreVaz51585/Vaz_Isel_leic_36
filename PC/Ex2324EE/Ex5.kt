

suspend fun <T,U> both( f1: suspend () -> T, f2: suspend () -> U): Pair<T,U> = coroutineScope {

    // NAO PRECISA DO TRY CATCH PORQUE JÁ O ASYNC AWAIT PROPAGA A EXCEPTION E O SCOPE CANCELA LOGO O OUTRO

    val d1 = async{
       try {
           f1()
       } catch (e : Exception){
           throw e
       }

    }

    val d2 = async{
        try {
            f2()
        } catch (e : Exception){
            throw e
        }

    }

    return d1.await() to d2.await()




}