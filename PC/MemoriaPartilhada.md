# Alocação de memória

Vale a pena existir concurrencia e metodos para tal, quando existe memória partilhada.
E existem 3 tipos de memória:

- memória global: Isto quando a variavél está definida globalmente. Zona de memória partilhada entre todas as threads de um processo 

- Stack: Quando são variavéis locais de uma função por exemplo. Zona de memória não partilhada entre todas as threads de um processo

- Heap: Area de memória reservada para alocação de variaveis, apenas conhecidas em tempo de execução. Essencialmente é uma zona de memória de alocação dinâmica. Zona de memória partilhada entre todas as threads de um processo 

Exemplo de um Heap:

val someList = ListOf(1,2,3,4,5)

A variavél someList que faz referencia à lista, está armazenada em Stack, enquanto que a lista está no Heap

Outro Exemplo:

    fun handleClient(socket: Socket, clientId: Int) {
        println("Client $clientId connected")
        socket.use { client ->
            val input = client.getInputStream().bufferedReader()
            val output = client.getOutputStream().bufferedWriter()
            try {
                var line: String?
                while (input.readLine().also { line = it } != null) {
                    output.write(line)
                    output.newLine()
                    output.flush()
                }
            } catch (e: Exception) {
                println("Error with client $clientId: ${e.message}")
            } finally {
                println("Client $clientId disconnected")
            }
        }
    }

Inicialmente temos que definimos a variavél output e portanto temos que numa primeira fase, output está armazenda em Stack, e faz referencia a client.getOutputStream().bufferedWriter() que por sua vez está armazenada em Heap.

Ao fazermos a chamada à variavél output dentro dos lambdas, chamado de closure, a variavél deixa de estar armazenada em Stack e passa a estar armazenda em Heap passando então a partilhar memória entre todas as threads


