import java.time.Duration
import java.util.LinkedList
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.remove
import kotlin.collections.removeFirst
import kotlin.compareTo
import kotlin.concurrent.withLock


class MessagePasser<T> {


    private val lock = ReentrantLock()

    private data class Request<T>(
        var message: T? = null,
        val cond: Condition,
        var isSignaled: Boolean = false,
        val thread: Thread
    )

    private val requests = LinkedList<Request<T>>()


    @Throws(InterruptedException::class)
    fun waitForMessage(timeout: Duration): T? {
        lock.withLock {

            val myRequest = Request<T>(cond = lock.newCondition(), thread = Thread.currentThread())
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {
                while (true) {

                    remaining = myRequest.cond.awaitNanos(remaining)

                    if (myRequest.isSignaled) return myRequest.message

                    if (remaining <= 0) {
                        requests.remove(myRequest)
                        return null
                    }

                }

            } catch (ie: InterruptedException) {
                if (myRequest.isSignaled) return myRequest.message
                else {
                    requests.remove(myRequest)
                    throw ie
                }
            }
        }

        fun passToCurrentFirstWaiter(message: T): Thread? {
            lock.withLock {

                if (requests.isEmpty()) return null

                val first = requests.removeFirst()
                first.message = message
                first.isSignaled = true
                first.cond.signal()

                return first.thread

            }

        }
    }


}