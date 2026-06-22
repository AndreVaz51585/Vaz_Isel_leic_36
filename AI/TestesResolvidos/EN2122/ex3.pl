Problema Eight-puzzle


# A 


p1 :(1, 2, 3, | 4, 5, 6, 7, 0, 8)


p2: (2, 4, 5, | 1, 0, 6, 8, 7, 3)


01: (1,2,3,1,0,6,8,7,3)

02: (2,4,5,4,5,6,7,0,8)


Obviamente que a solução gerada não é valida isto porque repetimos indices o que torna a resolução do eigh-puzzle impossivel.



# B 

i.

(1, 2,| 3, 4, 5, 6,| 7, 0, 8)
(2, 4,| 5, 1, 0, 6,| 8, 7, 3)

mapa 
3 - 5
4 - 1 
5 - 0
6 - 6 


01: 2 1 3 4 5 6 8 7 0
02: 4 2 5 1 0 6 7 3 8

ii.

(1, 2, 3, 4, 5, 6, 7, 0, 8)
(2, 4, 5, 1, 0, 6, 8, 7, 3)


p1[1] = 1 
p2[x] = 1, x = 4 

1 - 4 - 2  

p1[4] = 4 
p2[x] = 4 , x = 2 

p1[2] = 4


Ciclo 1 = [1,4,2]


p1[3] = 3
p2[x] = 3 , x = 9 

p1[9] = 8 
p2[x] = 8 , x = 7
p1[7] = 7
p2[x] = 7, x = 8

p1[8] = 0
p2[x] = 0, x = 5 

p1[5] = 5 
p2[x] = 5, x = 3 

ciclo 2 = [3,9,7,8,5]

ciclo 3 = [6]

Construção do P1-

Ciclo 1 vem do p1 {1,4,2} nestas posições escolhemos os genes do p1 

01 = 1 2 _ 4 _ _ _ _ _  


01 = 1 2 5 4 0 6 8 7 3                 



02 = Ciclo 1 = P2
     Ciclo 2 = P1
     Ciclo 3 = P2 


Posições 1,4,2 do P2 
Posição 3,9,7,8,5 do P1
posição 6 do P2 


02 = 2 4 3 1 5 6 7 0 8  


# C

Uma boa função de fitness seria atribuir 0 quando a peça não está no lugar correto, e 1 quando está, e quanto maior for o somatório de todos as peças mais proxima está de ser resolvida e consequentemente tem um fitness score mais elevado.



# D

Os elementos necessários para aplicarmos o algoritmo hill climbing são:

- uma função para gerar o estado inicial
- a função de fitness score para pudermos avaliar
- GenerateRandomNeighbor, para gerar os vizinhos
- GetBestNeighour, para obter o melhor vizinho mediante de todos os produzidos.
- check_done, uma função utilziada para verificar se o objetivo já foi atingido
- representação do estado.