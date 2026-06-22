

class PeriodicMessage(val message: String, val delayBetweenPresentations: Duration)

fun show(showString: (String) -> Unit, totalDuration: Duration, vararg periodicMessages: PeriodicMessage) = runBlocking  {

    val lock = Mutex()


        withTimeoutOrNull(totalDuration.toMillis()){

            for (msg in periodicMessages){

                 launch{

                     while (true){
                         lock.withLock{

                             showString(msg.message)

                         }
                         delay(msg.delayBetweenPresentations.toMillis())
                     }
                 }
            }
        }
}