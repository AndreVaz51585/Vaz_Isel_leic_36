

suspend fun <T, R> Executor.mapUntil(
    input: List<T>,
    f: (T) -> R,
    stopCondition: (R) -> Boolean
): List<R> {

    return suspendCoroutine {cont ->

        val list  = arrayOfNulls<R>(input.size)
        val lock = ReentrantLock()

        val partil = mutableListOf<R>()

        var done = 0

        var isDone = false


        for (i in input.indices){

            this.execute{

                val result = f(input[i])

                lock.withLock {
                    if(isDone) return@executor

                    list[i] = result
                    done++

                    if (stopCondition(result)) {
                        isDone = true
                        cont.resume(list.toList())
                    }

                    else if (done == input.size){
                        isDone = true
                        cont.resume(list.map{it as R})
                    }

                }


            }

        }




    }



}