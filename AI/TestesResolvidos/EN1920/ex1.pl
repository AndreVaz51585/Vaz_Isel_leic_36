
# A 

A representação de uma coordenada é feita via (Col,Row) e temos portanto de representar o Sokoban, a caixa, os muros/walls, e a posição final, goal e portanto podemos representar a situação inicial
e final da seguinte forma: 


Inicial: 

state(sokoban(11,8), box(5,7))

Final : 
state(box(17,7)) A posição do sokoban final não é relevante desde que a Box esteja no local desejado. A representação pode ser feita também da seguinte forma:
[sok(11,8), box(5,7)]



As ações do sokoban possiveis são :
move(sok, N), move(sok, S), move(sok, E), move(sok, W),
move(sok, box, N), move(sok, box, S), move(sok, box, E), move(sok, box, W)




Só precisamos de representar neste state o sokoban e a box, porque as wall e posições finais são imutavéis e podem portanto ser representandas noutro predicado
 
Solução final:

A representação de um estado no espaço de estados é feito atravês de uma localização (x,y) para cada caixa, assumimos para já somente uma caixa, e uma localização (x,y) para o sokoban.
TAmbém temos de considerar as paredes e a posição final, contudo como estas localizações são imutavéis podem ser consideradas por outros predicados.

Em cada passo, o sokoban pode aplicar um movimento com e sem caixa, nas direções N,S, E, O represetandas da seguinte forma:
move(sok,N)...
move(sok,box,N)...

Dito isto, os estados inicial e final são respetivamente [sok(11,8), box(5,7)] e [box(17,7)]


# B 

Para operadores vamos necessitar: 
- verififcar posição livre
- verififcar se há parede.
- verificar se há caixa
- encontrar jogador no tabuleiro.
- validar movimento numa direção sem caixa
- remover sokoban da posição anterior 
- aplicar movimento numa direção sem caixa
- adicionar sokoban na nova posição
- validar movimento numa direção com caixa
- aplicar movimento numa direção com caixa
- validar se a caixa está na posição final desejada.
- verificar colisões com paredes e deadlocks


% operador principal
s(State1, State2)

% Direções 
direction(n).
direction(s).
direction(o).
direction(e).

coords(X,Y,Dir,X1,Y1)

% verificar se uma posição está livre
free_pos(X, Y, State).

% verificar se há parede
wall(X, Y).

% verificar se há caixa
box_at(X, Y, State).

% verificar se há sokoban
sok_at(X, Y, State).

% substituir/remover/adicionar elementos no estado
remove_sok(State, NewState).
remove_box(X, Y, State, NewState).
add_sok(X, Y, State, NewState).
add_box(X, Y, State, NewState).

% operador usado pela procura
s(State,NewState):-
    direction(Dir),
    move_without_box(State,Dir,NewState).


s(State,NewState):-
    direction(Dir),
    push_box(State,Dir,NewState).

% direções e coordenadas
direction(Dir).
coords(X,Y,Dir,X1,Y1)

coords(X,Y,n,X1,Y1):-
    Y1 is Y - 1.

coords(X,Y,s,X1,Y1):-
    Y1 is Y + 1.


coords(X,Y,o,X1,Y1):-
    X1 is X - 1.

coords(X,Y,e,X1,Y1):-
    X1 is X + 1.


% leitura do estado
find_sokoban(State, X,Y):-
    member(sok(X,Y), State)


box_at(X,Y,State):-
    member(box(X,Y), State).


wall(X,Y).



free_pos(X,Y,State) :-
   \+ wall(X,Y) ,
   \+ box_at(X,Y,State),
   \+ find_sokoban(State,X,Y). 



% validação
can_move_without_box(State,Dir):-
    find_sokoban(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    free_pos(X1,Y1,State).


can_push_box(State,Dir):-
   find_sokoban(State,X,Y),
   coords(X,Y,Dir,X1,Y1),
   box_at(X1,Y1,State),
   coords(X1,Y1,Dir,X2,Y2),
   free_pos(X2,Y2,State).



% aplicação
move_without_box(State,Dir,NewState):-
    can_move_without_box(State,Dir),
    find_sokoban(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    remove_sok(State,DelState),
    add_sok(X1,Y1,DelState,NewState).


push_box(State,Dir,NewState):-
    can_push_box(State,Dir),
    find_sokoban(State,X,Y),
    coords(X,Y,Dir,XBox,YBox),
    coords(XBox,YBox,Dir,XNew,YNew),
    remove_sok(State,SokDeleted),
    remove_box(XBox,YBox,SokDeleted,SokAndBoxDeleted),
    add_sok(XBox,YBox,SokAndBoxDeleted,SokAdded),
    add_box(XNew,YNew,SokAdded,NewState).



% manipulação do estado
remove_sok(State,StateWithoutSok):-
    find_sokoban(State,X,Y),
    del(sok(X,Y),State,StateWithoutSok).  


remove_box(X,Y,State,StateWithoutBox):-
    box_at(X,Y,State),
    del(box(X,Y),State,StateWithoutBox).



add_sok(X,Y,State,NewState):-
    add(sok(X,Y),State,NewState).


add_box(X,Y,State,NewState):-
    add(box(X,Y),State,NewState).


del(X,[X|T],T).

del(X,[H|T],[H|R]):-
    del(X,T,R).


add(X, L1,[X|L1]).


% objetivo
goal(State):-
    final_positions(Finals),
    all_boxes_in_final_positions(Finals,State).


all_boxes_in_final_positions([], _State).


all_boxes_in_final_positions([Box_pos|Rest],NewState):-
    member(BoxPos,State),
    all_boxes_in_final_positions(Rest,State).



final_positions(List).

final_positions([
    box(2,2)
]).

walls([
    wall(0,0), wall(1,0), wall(2,0), wall(3,0),
    wall(0,1),                     wall(3,1),
    wall(0,2),                     wall(3,2),
    wall(0,3), wall(1,3), wall(2,3), wall(3,3)
]).


all_boxes_in_final_positions(List,State).


% otimização/opcional
deadlock(X,Y).



final_positions([
    goal(2,2),
    goal(3,4)
]).

goal(State) :-
    final_positions(Finals),
    boxes_on_goals(Finals, State).

boxes_on_goals([], _).

boxes_on_goals([goal(X,Y)|Tail], State) :-
    member(box(X,Y), State),
    boxes_on_goals(Tail, State).



#C


A arvore de procura seria a seguinte:

Estado inicial:
  state(sokoban(11,8), box(5,7))

Considerando as possiveis Direções:

1.N
2.S
3.O
4.E

iter = 0    
State 1: North (1). 


iter = 1
State 1.1: West (2)
State 1.2: East (3)

iter = 2
State 1.1.1 : West(4)
State 1.2.1 : East(5)

iter = 3 
State 1.1.1.1 : South(6)
State 1.1.1.2 : West(7)
State 1.2.1.1 : East(8)

iter = 4
State 1.1.1.1.1 : South(9)
State 1.1.1.2.1 : North(10)
State 1.1.1.2.2 : West(11)


