

fun <T1, T2> both(f1: suspend ()->T1, f2: suspend ()->T2, timeout: Duration): Pair<T1, T2>? = runBlocking {

    withTimeoutOrNull(timeout){

    val value1 = async { f1() }
    val value2 = async { f2() }


    return value1.await() to value2.await()

    }
}



