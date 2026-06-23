# A

p1: (9, 3, 1, | 4, 7, 2, 6, 8, 5)

p2: (6, 1, 5, | 2, 9, 3, 7, 4, 8)


01 : (9,3,1,2, 9, 3, 7, 4, 8) p1 - intervalo do p2 
02 : (6, 1 , 5 , 4 , 7 ,2 ,6 ,8, 5) - p2 com intervalo do p1 


Sim, podemos usar, contudo pode produzir uma solução inválida, mas uma vez que solução inválidas podem ser consideradas então é possivel.



# B 

i. 

p1: (9, 3,| 1, 4, 7,| 2, 6, 8, 5)

p2: (6, 1,| 5, 2, 9,| 3, 7, 4, 8)


01 :   | 1,4,7| 

3 , 8 , 6 , 5 , 2 , 9

2, 9  | 1 , 4 ,7 | 3 , 8 , 6 , 5


02 :  6 ,8 ,3 ,1 ,4 ,7

  4 , 7 | 5 2 9 | 6 , 8 , 3 1   

ii.


p1[1] = 9 
p2[x] = 9 , x = 5 

p1[5] = 7
p2[x] = 7, x = 7

p1[7] = 6
p2[x] = 6, x = 1

1 ciclo = {1,5,7}

p1[2] = 3
p2[x] = 3, x = 6

p1[6] = 2
p2[x] = 2, x = 4

p1[4] = 4 
p2[x] = 4, x = 8

p1[8] = 8
p2[x] = 8, x = 9

p1[9] = 5 
p2[x] = 5, x = 3 

2 ciclo = {2,6,4,8,9}

p1[3] = 1 
p2[x] = 1, x = 2 

3 ciclo = {3}


Isto para 01:
P1 = ciclo1 
p2 = ciclo2
P1 = ciclo1 

Para 02:
P2 = ciclo1
P1 = ciclo2
P2 = ciclo1

01 : 911273648
02 : 635492785  



# C 

Aqui uma boa função de fitness é contar o numero de valores repetidos, portanto quanto menos numeros repetidos tiver, idealmente sem repetições, maior será o seu fitness score. 

Por exemplo a solução 1,2,3,4,5,6,7,8,9 teria score maximo isto porque não repete nenhum valor.

# D

- Uma função que permite gerar o estado inicial.
- Uma função de fitness, por exemplo a que definimos em cima.
- GenerateNNeighbours, a função responsavél por gerar N vizinhos
- getBestNeighbour, a função responsavél por selecionar o melhor candidato/vizinho.
- uma função que representa o estado.
