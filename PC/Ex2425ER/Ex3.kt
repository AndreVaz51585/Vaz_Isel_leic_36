class UnaryCapacityStream<T> {

    private val lock = Mutex()

    private var currentValue : T? = null
    private var indexCounter = -1

    private data class Request(val index : Int, val cont : Continuation<Pair<Int,T>>)

    private val requests = LinkedList<Request>()

    suspend fun add(item: T): Int {
        lock.withLock{

                if(currentValue == null) currentValue = item


                indexCounter ++


            // Fast path, there is no requests
            if(requests.isEmpty()){
               return indexCounter
            }

            // Slow Path there is pendingRequests but we only answer to it if the index <= indexCounter, otherwise
                for request in requests {
                    if(request.index <= indexCounter){
                         requests.remove(request)
                         request.cont.resume(indexCounter to value)
                    }
                }

            }

        }
    }

    // ERRO Importante, não podemos usar o withLock, tem de ser lock e unlock manual
    suspend fun read(index: Int): Pair<Int, T>{
        lock.withLock{

          // fast path, the queue already has a value and indexCounter >= index
          if(indexCounter >= index){
              val value = queue.peekFirst()
              return indexCounter to value
          }

            // slow path we must wait for a element that we want

            return suspendCourotine{ cont ->
                   val myRequest = Request(index,cont)
                   requests.addLast(myRequest)
            }
        }
    }
}

