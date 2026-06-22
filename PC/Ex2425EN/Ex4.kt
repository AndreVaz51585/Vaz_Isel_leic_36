
class PeriodicMessage(
    val message: String,
    val delayBetweenPresentations: Duration)

    private val lock = Mutex()


    fun show(showString: (String) -> Unit, totalDuration: Duration, vararg periodicMessages: PeriodicMessage) = runBlocking {

        withTimeOutOrNull(totalDuration) {

            for periodicMessage in periodicMessages{
                  launch{
                        while(true){
                            lock.withLock{
                               showString(periodicMessage.message)
                            }
                            delay(periodicMessage.delayBetweenPresentations)
                        }
                  }
            }


        }

    }



