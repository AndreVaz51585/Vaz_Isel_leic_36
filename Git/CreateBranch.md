# How to create a git Branch, switch to it, checkout, etc

## Step by Step : 

    git status      // permite validarmos o estado atual do nosso repo locl

    git branch feature  // cria um branch local chamada de feature

    git branch -a // permite observar as branches existentes, sendo a que tem o asterisco a nossa branch atual

    git push -u origin feature // informa o github da existencia desta nova branch

    git checkout feature  // permite-nos trocar de branch para a feature

    OU 

    git checkout -b feature // para criar e trocar automaticamente para essa branch


Esta Serie de instruções seguintes, permite enviar o código que está na main atual para a branch especificada.

    git checkout feature-branch
    git pull
    git rebase main