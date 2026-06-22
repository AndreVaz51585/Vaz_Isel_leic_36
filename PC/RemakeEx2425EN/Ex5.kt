
suspend fun <T1,T2> Executor.invoke(f1: ()->T1, f2: ()->T2): Pair<T1,T2> {

    return suspendCoroutine { cont ->

        val lock = ReentratLock()

        var valueA : T1? = null
        var valueB : T2? = null

        var hasValueA : Boolean = false
        var hasValueB : Boolean = false


        this.execute{

              val result = f1()

              lock.withLock{

                  valueA = result
                  hasValueA = true

                  if(hasValueB){
                      cont.resume(valueA as T1 to valueB as T2)
                  }

              }
        }

        this.execute{

            val result = f2()

            lock.withLock{

                valueB = result
                hasValueB = true

                if(hasValueA){
                    cont.resume(valueA as T1 to valueB as T2)
                }

            }
        }





    }





}