class Node<T>(val data: T, val next: Node<T>?)
class UnsafeListSuccession<T>(
    private val listHead: Node<T>?
){
    private val lock = ReentrantLock()
    private var currNode = listHead
    fun next(): T? {
        lock.withLock{
            if(currNode != null) {
                val res = currNode.data
                currNode = currNode.next
                res
            } else {
                null
            }

        }


    }
}
