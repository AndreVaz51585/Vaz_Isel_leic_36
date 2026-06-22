class UnsafeStack<T> {

    class Node<T>(val value: T, val next: Node<T>?)

    private val lock = ReentrantLock()
    private var head: Node<T>? = null



    fun push(value: T) {
        lock.withLock{
            head = Node(value, head)
        }
    }


    fun pop(): T? {
        lock.withLock{
            val observedHead = head
            return if(observedHead != null) {
                head = observedHead.next
                observedHead.value
            } else {
                null
            }
        }
    }
}