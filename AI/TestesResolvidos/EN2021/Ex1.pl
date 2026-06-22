# A 

 
A representação de um estado no espaço de estados é feita atravês de uma coordenada/localização (col,row)/(x,y) para cada pellet que cobre o tabuleiro do jogo, e da localização atual do Pacman.
Também devem ser consideradas as walls, contudo como estas são imutavéis podem ser consideradas por outros predicados. A posição final do pacman não é relevante, a vitória é verificada quando não existir nenhuma pellet no tabuleiro.

O pacman pode a cada ação, mover-se uma posição nas direções N,S,E,O tendo em conta obviamnete as paredes.
Portanto teriamos o predicado move(pac,Dir) sendo Dir todas as possiveis direções e um predicado para comer uma pellte eat(pac,Dir,Pellet), sendo pellet a posição da pellet claro.

A representação do estado inicial seria a posição do pacman portanto : [pac(13,30), ...], e todas as posições das pellets que cobrem o mapa. 

a representação do estado final é somente um tabuleiro sem qualquer pellets, ou seja resulta num array vazio essencilamente.




# B
 operações que temos de ter:

- encontrar a posição do pacman atual
- validar movimento
- aplicar movimento
- aplicar remoção da pellet
- remover pacman 
- adicionar pacman
- verificar se tem uma parede 
- verificar se já terminou (lista de pellets vazia)

% operador principal
s(State1, State2):-
    direction(Dir),
    applyMove(State1,Dir,State2)

% Direções 
direction(n).
direction(s).
direction(e).
direction(o).

wall(X,Y)

coords(X,Y,Dir,X1,Y1).

coords(X,Y,n,X1,Y1) :-
  Y1 is Y - 1   


coords(X,Y,s,X1,Y1) :-
  Y1 is Y + 1


coords(X,Y,e,X1,Y1) :-
  X1 is X + 1  


coords(X,Y,o,X1,Y1) :-
  X1 is X - 1


find_pacman(State,X,Y):-
    member(pac(X,Y),State).


pellet_at(X,Y,State):-
    member(pellet(X,Y),State).

wall(X,Y):-
    walls(Walls),
    member(wall(X,Y),Walls).    


can_move(State,Dir):-
    find_pacman(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    \+ wall(X1,Y1).

apply_move(State,Dir,NewState) :-
    can_move(State,Dir),
    find_pacman(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    pellet_at(X1,Y1,State),
    remove_pellet(X1,Y1,State,State1),
    remove_pacman(X,Y,State1,State2),
    add_pacman(X1,Y1,State2,NewState).


apply_move(State,Dir,NewState) :-
    can_move(State,Dir),
    find_pacman(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    \+ pellet_at(X1,Y1,State),
    remove_pacman(X,Y,State,State1),
    add_pacman(X1,Y1,State1,NewState).


goal(State) :-
    \+ pellet_exists(State).


pellet_exists(State):-
    member(pellet(_,_),State).    





remove_pacman(X,Y,State,NewState):-
    delete(pacman(X,Y), State, NewState).


remove_pellet(X,Y,State,NewState):-
    delete(pellet(X,Y), State, NewState).


add_pacman(X,Y,State,NewState):-
    add(pacman(X,Y),State,NewState).    


delete(X, [X|R], R).

delete (X, [H|T], [H|R]):-
    delete(X, T, R).

add(X, L1, [X|L1]).    



# C


Initial Pos : pacman(11,30)
Directions : N, S , W, E

Iter 1: 

State 1.1 West (1).   PASSAR A METER AS coordenadas
State 1.2 East (2).

Iter 2: 

State 1.1.1 North (3).
State 1.1.2 West (4).
State 1.2.1 East (5).


Iter 3:
State 1.1.1.1 North(6).
State 1.1.1.2 West(7).
State 1.2.1.1 North(8).
State 1.2.1.2 East(9).

Iter 4:
State 1.1.1.1.1 North(10).


O algoritmo best first search utiliando sem heuristica, é igual ao breadth-first. Com heuristica, sendo possivlemnte a distancia do pacman a pellets ainda não comidas, iria melhorar a procura.