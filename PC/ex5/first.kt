

suspend fun <T> Executor.first(
    f1: () -> T,
    f2: () -> T
): T {

    return suspendCoroutine {cont ->

        val lock = ReentrantLock()

        var isDone = false

        execute{
           val result = f1()
            lock.withLock{
                if (!isDone){
                    isDone = true
                    cont.resume(result)

                }
            }
        }


        execute{
            val result = f2()
            lock.withLock{
                if (!isDone){
                    isDone = true
                    cont.resume(result)
                }
            }



    }




}