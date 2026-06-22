
class UnsafeCyclicSuccession<T>(
    private val items: Array<T>
){
    private val lock = ReentrantLock()

    private var index = 0

    fun next(): T {
        lock.withLock{
            val res = items[index]
            index += 1
            if(index == items.size) index = 0
            return res
        }
    }
}
