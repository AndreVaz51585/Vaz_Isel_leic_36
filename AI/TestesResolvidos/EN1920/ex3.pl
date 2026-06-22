# A

Não, isto porque o Uniform CrossOver implica a repetição de genes, o que torna o cromossoma inválido para a solução de TSP. 

# B

i

Order CrossOVer 

Os Filhos produzidos têm de ser obrigatoriamente diferentes dos pais, caso contrário a recombinação não faz sentido.

P1 - (4,|3,1,2|,5)   01 - ( 5,3,1,2,4)

P2 - (4,|1,5,2,|3).  02 - (3,1,5,2,4) 



ii.

Cycle CrossOver : 


(4,3,1,2,5)

(4,1,5,2,3)

Primeiro ciclo :
4 - 4

Segundo ciclo
3 -> 1 
1 -> 5
5 -> 3 

Terceiro ciclo 
2 - 2 

Agora usamos os ciclos de cada parent   4 3 1 2 5 
                                        4 1 5 2 3 

e preenchemos as restantes posições com os genes do parent contrário


# C

Uma boa função de fitness é a distancia percorrida no total, e portanto distancias menores vão ter maior fitness, calculda através da soma total das arestas dos genes das rotas.



# D

Para desenvolver o algoritm de Hill climbing vou precisar de:

-> função para gerar o estado inicial 
-> uma função de avaliação, ou seja uma função de fitness apropriada para avaliar os candidatos.
-> operador de vizinhança,(GenerateRandomNeighbor) responsavél por gerar N vizinhos, onde especifico de que forma são gerados.
-> critério de paragem, para parar quando não encontrarmos um vizinho melhor.
-> GetBestSolution, que dentro de um array de vizinhos me permite escolher o melhor vizinho. 

