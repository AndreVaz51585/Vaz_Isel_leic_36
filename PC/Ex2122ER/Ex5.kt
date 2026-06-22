

suspend fun <A,B,C> run(f0: suspend ()->A, f1: suspend ()->B, f2: suspend (A,B)->C): C = coroutineScope {

    val d0 = async{f0()}
    val d1 = async{f1()}

    return f2(d0.await(), d1.await())

}