
class Box<T: Any> {

    private val lock = Mutex()

    private data class ReadRequest<T>(val cont : Continuation<T>)

    private val requests = LinkedList<ReadRequest<T>>()

    private var CurrValue : T? = null


    suspend fun read(): T {
        lock.lock()

        if(CurrValue != null){
            return CurrValue
            lock.unlock()
        }

        return suspendCoroutine { cont ->
              val myRequest = ReadRequest<T>(cont)
              requests.addLast(myRequest)
              lock.unlock()
        }

    }

    suspend fun set(value: T, timeToLive: Duration): Unit {
         val toResume = mutableListOf<ReadRequest<T>>()

        lock.withLock{


        CurrValue = value

             if(requests.isNotEmpty()) {
                 toResume = requests
                 requests.clear()
             }
         }

          toResume.forEach{it.cont.resume(value)}
          return

    }

    suspend fun clear(): Unit {
        lock.withLock{
              CurrValue = null
              return

        }
    }
}