package pt.isel.pc.assignment1

import java.time.Duration
import java.util.LinkedList
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class MessagePasser<T> {

    private val lock = ReentrantLock()

    // É melhor usarmos uma variavél delivered booleana ? porque T pode ser nullable.
    private data class Request<T>(val cond : Condition, val thread : Thread, var message : T? = null )

    private val requests = LinkedList<Request<T>>()



    @Throws(InterruptedException::class)
    fun waitForMessage(timeout: Duration): T? {
        lock.withLock{

            val myRequest = Request<T>(lock.newCondition(), Thread.currentThread())
            requests.addLast(myRequest)

            var remaining = timeout.toNanos()

            try {

                while (true){

                    remaining = myRequest.cond.awaitNanos(remaining)

                    if(myRequest.message != null) return myRequest.message

                    if(remaining <= 0){
                        requests.remove(myRequest)
                        return null
                    }


                }


            } catch (ie: InterruptedException) {
                if(myRequest.message != null) return myRequest.message
                else {
                    requests.remove(myRequest)
                    throw ie
                }
            }


        }
    }

    fun passToAtMostNWaiters(maxWaiters: Int, message: T): List<Thread> {
        lock.withLock{

            // fast path: there is no requests so we return 0

            if(requests.isEmpty()) return emptyList()

            var counter = 0
            val list = mutableListOf<Thread>()

            while (counter < maxWaiters && requests.isNotEmpty()) {
                val first = requests.removeFirst()
                first.message = message
                list.add(first.thread)
                first.cond.signal()
                counter++
            }

            return list

        }
    }
}