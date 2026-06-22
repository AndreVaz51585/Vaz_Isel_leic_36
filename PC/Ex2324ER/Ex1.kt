class UnsafePairSuccession<T>(
    private val items: Array<T>
) {
    private var currIndex = 0

    private val lock = ReentrantLock()


    fun nextConsecutiveItemsPair(): Pair<T, T>? {
        lock.withLock{
            if (currIndex + 2 > items.size) return null
            val first = items[currIndex++]
            val second = items[currIndex++]
            return Pair(first, second)
        }
    }
}