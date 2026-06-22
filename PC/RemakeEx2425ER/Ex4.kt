

fun <T1, T2> both(f1: suspend ()->T1, f2: suspend ()->T2, timeout: Duration): Pair<T1, T2>? = runBlocking{


    withTimeoutOrNull(timeout.toMillis()){

        val d1 = async{ f1()}
        val d2 = async{ f2()}



        return d1.await() to d2.await()

    }

}