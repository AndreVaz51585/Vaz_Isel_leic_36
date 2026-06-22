


suspend fun <T1,T2> Executor.invoke(f1: ()->T1, f2: ()->T2): Pair<T1,T2> {

    var value1 : T1? = null
    var value2 : T2? = null

    var done = 0

    val lock = ReentrantLock()


   return suspendCoroutine { cont ->

        execute{
            var r1 = f1()
            lock.withlock{
                value1 = r1
                done ++
                if(done == 2 ) cont.resume(value1 as T1 to value2 as T2)
            }
        }

        execute{
            var r2 = f2()
            lock.withlock{
                value2 = r2
                done ++
                if(done == 2 ) cont.resume(value1 as T1 to value2 as T2)
            }
        }
    }
}