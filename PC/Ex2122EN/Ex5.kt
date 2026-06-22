


suspend fun race(f0: suspend () -> Int, f1: suspend () -> Int): Int {

   val value0 = aysnc{ f0() }

   val value1 = async { f1() }


    val result = select<Int> {

        value0.onAwait { result ->
            value1.cancel()
            result
        }

        value1.onAwait { result ->
            value0.cancel()
            result
        }

    }

    value0.join()
    value1.join()

    result

}


