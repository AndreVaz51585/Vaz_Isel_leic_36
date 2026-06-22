
suspend fun <T,R> Executor.map(f: (T)->R, input: List<T>): List<R> {

    suspendCoroutine{ cont ->

        private val lock = ReetrantLock()
        var completed = 0
        val results = arrayOfNulls<Any?>(input.size)


        for index in input.indices{
            execute{
                val result = f(input[index])

                lock.withLock{

                  results[index] = result
                  completed ++


                if(completed == input.size) cont.resume(results.toList() as List<R>)

                }



            }


        }

    }

}