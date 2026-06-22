
suspend fun waitForN(
    n: Int,
    actions: List<suspend () -> Unit>
): Boolean = coroutineScope {

    var completed = 0

    val lock = Mutex()

    for (action in actions){

        launch{

                try {
                     action()
                } catch (e : Exception){




                }





        }


    }






}