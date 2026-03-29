# Notas importantes 


## Execução periodica de uma task

* criação de uma thread virtual 


        private val scheduler = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory)


* Uma possivel simplificação da função schedule 

        fun ScheduleExecutorService.schedule(delay : Duration = Duration.zero , comand : () -> Unit ) =

        this.schedule(command, Delay.inWholeMilliseconds, TimeUnit.MILLISECONDS)




