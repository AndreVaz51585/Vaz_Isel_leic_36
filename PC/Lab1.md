# Explicação Lab1 e algumas notas importantes


    fun startAndObserve (thread : Thread){

        var prevState = thread.state


        println("<${System.currentTimeMillis()}> <${thread.name}> <${thread.state}> ")
        thread.start()

        while (thread.state != Thread.State.TERMINATED){
            val curr = thread.state
            if(curr != prevState) {
                println("<${System.currentTimeMillis()}> <${thread.name}> <$curr>")
                prevState = curr
            }
                Thread.sleep(500)
        }

        println("<${System.currentTimeMillis()}> <${thread.name}> <${thread.state}> ")
    }


    fun main() {
        val thread = Thread({
            Thread.sleep(500)
        }, "Test Thread")

        val sleeper = Thread({
            println("Starts")
            Thread.sleep(2000)
            println("Ends")
        }, "Sleeper")


        startAndObserve(sleeper)
    }

## Main Thread

Aqui neste troço de código a Main Thread é responsável por correr o código da função Main, cria ambas as threads e chama a função startAndObserve.
Dentro desta, começa então a sleeper Thread e entra no while onde fica avaliar o estado da mesma e adormecer(Thread.sleep(500)). Avalia o estado e consoante alguma mudança de estado, ela imprime continua até o estado Terminated.


## Sleeper Thread
Começada pela Main Thread, executa o respetivo código e termina.