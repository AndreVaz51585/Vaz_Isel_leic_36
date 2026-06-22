
suspend fun <T> allWithinTimeout(
    timeoutMillis: Long,
    providers: List<suspend () -> T>
): List<T>? = coroutineScope {

    require(providers.isNotEmpty())

    val lock = Mutex()

   withTimeoutOrNull(timeoutMillis){

       val deffered = providers.map{ it ->
           async{it()}
       }

       deffered.map {it ->
           it.await()
       }
   }

}