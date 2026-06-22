# Pontos e Notas importantes deste segundo Lab

## Data Paralelism

Consiste essencialmente em dividir informação, para ser trabalhada, por threads distintas.

Isto é possivel e permite a sincronização ao realizarmos o **.join()**.

## Embarrasing-Parallel

Quando um problema, pode ser dividido em tarefas diferentes realizados por diferentes threads sem ser necessário comunicação entre elas, ou seja não existe **dependencias**.

## Reduce Pattern

Consiste em combinar multiplos resultados parciais, num resultado final.

## Race condition

Ocorre quando multiplas threads, tentam aceder ou modificar um dado sem sincronização.