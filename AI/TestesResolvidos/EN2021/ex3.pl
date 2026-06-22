# A

Sim, porque caminhos inválidos podem ser considerados.

Exemplo :

Gerarmos uma string random com tamanho igual aos cromossomas de 1 e 0 sendo 1 escolher um gene do pai1 e 0 escolher um gene do pai2

100110111001010

Resulta em :

NNEN....



# B

i.

NSN|EWSENNN|SSSEW
ENN|NNNENEN|ESSSE


NSN NNNENEN SSSEW

Só temos somente um dos valores em conta.

ii. Podemos usar o swap mutation, onde simplesmente trocamos as posições de dois genes por exemplo NSNEWSENNNSSSEW fica WSNEWSENNNSSSEN trocando o priemiro e ultimo gene de lugar.

Podiamos também aleatoriamente simplesmente subsituir uma direção.


# C

Uma função de fitness possivel seria por exemplo, contar o numero de posições/movimentos necessários ainda por atingir para conseguir resolver, no caso a proximidade da saida do labirinto.

Ou então atribuir um peso à cada movimentação e caso produza movimentos inválidos é atribuido um valor baixo.


# D

Para conseguirmos aplicar corretamente o algoritmo de hill-climbing vamos precisar de:

- Uma função para gerar a função inicial.
- da respetiva função fitness para conseguirmos avaliar os candidatos
- uma função, GenerateRandomNeighbor que gera vizinhos de forma aleatoria
- uma função, GetBestNeighour que seleciona a melhor o melhor candidato/vizinho dos disponivéis.
- Uma função para representação da solução.




