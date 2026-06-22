class UnsafeSuccession<T>(
    private val items: Array<T>
){

    private val lock = ReentrantLock()
    private var index = 0


    fun next(): T? = lock.withLock {
        if(index < items.size) {
            items[index++]
        } else {
            null
        }
    }