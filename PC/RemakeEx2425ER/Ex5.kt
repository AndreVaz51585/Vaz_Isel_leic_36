

suspend fun <T,R> Executor.map(f: (T)->R, input: List<T>): List<R> {


    return suspendCoroutine { cont ->


        val list = arrayOfNulls(input.size){}
        val lock = ReentrantLock() // ou mutex()
        var done = 0

        for (i in input.indices) {

            this.execute {

             val result = f(input[i])

             lock.withLock{
                 list[i] = result
                 done ++
                    if(done == input.size) cont.resume(list.toList() as List<R>)
             }
            }

        }
    }

}