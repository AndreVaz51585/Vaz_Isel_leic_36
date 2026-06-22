
class UnsafeModuloCounter(
    private val modulo: Int,
) {

    private val lock = ReentrantLock()

    init { require(modulo > 1) }
    private var counter: Int = 0
    fun increment(): Int {
        lock.withLock{

            counter += 1
            if (counter == modulo) {
                counter = 0
            }
            return counter
        }
    }
}