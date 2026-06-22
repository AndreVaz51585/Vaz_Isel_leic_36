

class UnaryCapacityStream<T> {

    private var currItem : T? = null

    private var currentIndex : = -1

    private var hasItem = false

    private data class Request<T>(val cont : Continuation<Pair<Int,T>> , val readIndex : Int)

    private val requests = LinkedList<Request<T>>()

    private val lock = Mutex()




    suspend fun add(item: T): Int {
        val toResume = mutableListOf<Request<T>>()
        val index: Int

        lock.withLock {

            currentIndex++
            currItem = item
            index = currentIndex


            val iter = requests.iterator()

                while (iter.hasNext()){

                    val request = iter.next()

                    if(request.readIndex <= currentIndex){
                        iter.remove()
                        toResume.add(request)
                    }
                }

        }

        toResume.forEach{it.cont.resume( index to item)}
        return index


    }



    suspend fun read(index: Int): Pair<Int, T> {
        lock.lock()

        val item = currItem


        if(currentIndex >= index && item != null){
            lock.unlock()
            return currentIndex to item
        }


        return suspendCoroutine { cont ->

            val myRequest = Request(cont, index)
            requests.addLast(myRequest)
            lock.unlock()
        }

    }
}