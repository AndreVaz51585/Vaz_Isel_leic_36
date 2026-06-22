
# A 

Para a reprentação de um estado no espaço de estados, é necessário existir uma localização (x,y) por cada caixa que existe, é necessário também atribuir uma localização (x,y) a bola amarela.
Temos ainda de ter em consideração as paredes, os buracos e  portas de saida etc, contudo uma vez que estess são imutavéis podem ser considerados por outro predicado.
A Bola e as caixas podem mover-se nas direções N,S,O,E.
Para isto temos portanto dois predicados principais a ter em conta:
move(sokoball,Dir) sendo Dir uma das possiveis direções e push(sokoball,box,Dir).

Portanto a representação do estado inicial deve ser feita [sokoball(13,3), box(...), switch(...)] sendo ... a localização de todas as caixas presentes

A representaçãpo do estado final acaba por ser lista com as posições finais das caixas que devem ser concidir com as posições das portas. Aqui é imporntate não termos uma lista vazia isto porque temos a pressença de buracos que corresponde à derrota e portanto é imporatnte realçar essa diferença.
A posição final do sokoball não é relevante.

Final = [box(X,X),box(..)] sendo X,Y e as coordenadas das boxes coincidenetes com as das portas.



#B



direction(n).
direction(s).
direction(e).
direction(o).


s(State,NewState):-
    direction(Dir),
    move_without_box(State,Dir,NewState).


s(State,NewState):-
    direction(Dir),
    push_box(State,Dir,NewState).



wall(x,y)

walls([wall(..,..)]) ... lista de walls 

holes([hole(..,.)]) ... Lista de Holes




wall(X,Y):-
    walls(Walls)
    member(wall(X,Y), Walls)

hole(X,Y):-
    holes(Holes),
    member(hole(X,Y),Holes).

doors([door(...,...), door(...,...)]).

door(X,Y) :-
    doors(Doors),
    member(door(X,Y), Doors).    


coords(X,Y,Dir,X1,Y1).

coords(X,Y,n,X1,Y1):-
 Y1 is Y -1.    

coords(X,Y,s,X1,Y1):-
 Y1 is Y + 1.    

coords(X,Y,o,X1,Y1):-
 X1 is X -1.    


coords(X,Y,e,X1,Y1):-
 X1 is X + 1.    

goals([
    goal(..,..)
])


delete(X, [X|R], R).

delete(X, [H|R],[H|L]):-
    delete(X,R,L).

add(X,L1,[X|L1]).


remove_sokoball(X,Y,State,NewState):-
    delete(sokoball(X,Y),State,NewState).

remove_box(X,Y,State,NewState):-
    delete(box(X,Y),State,NewState).

add_sokoball(X,Y,State,NewState):-
    add(sokoball(X,Y),State,NewState).

add_box(X,Y,State,NewState):-
    add(box(X,Y),State,NewState).




sokoball(x,y)
box(x,y)
hole(x,y)

check_goals(State):-
    all_boxes_onGoals(State).


all_boxes_onGoals(State):-
    forall(member(box(X,Y),State), goal(X,Y)).


find_sokoball(State,X,Y):-
    member(sokoball(X,Y),State).


box_at(X,Y,State):-
    member(box(X,Y),State).        


check_lose(State):-
    # aqui temos de verificar se a posição final do sokobal é igual a algum dos Holes 

    forall(member(box(X,Y),State), hole(X,Y))    


Em ambos os can_move or can_push não verificamos se a bola ou caixa cai para dentro do buraco, isto porque isso é estado possivel e não um estado inválido, no sucessor, ao verificarmos conduição de vitória temos de verfificar também a condição de derrota para verfificar se a caixa ou sokoball cai no buraco.

can_move_without_box(State,Dir):-
    find_sokoball(State,X,Y)
    coords(X,Y,Dir,X1,Y1),
    \+ walls(X1,Y1).
    \+ holes(X1,Y1),
    \+ box_at(X1,Y1),
    \+ active_fence(X1,Y1,State)

can_push_box(State,Dir):-
    find_sokoball(State,X,Y)
    coords(X,Y,Dir,X1,Y1),
    box_at(X1,Y1,State),
    coords(X1,Y1,Dir,X2,Y2),
    \+ wall(X2,Y2),
    \+ hole(X2,Y2),
    \+ box_at(X2,Y2,State),
    \+ active_fence(X2,Y2,State).


move_without_box(State,Dir,NewState):-
    can_move_without_box(State,Dir),
    find_sokoball(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    remove_sokoball(X,Y,State,DeletedSokoball),
    add_sokoball(X1,Y1,DeletedSokoball,NewState).


push_box(State,Dir,NewState):-
    can_push(State,Dir),
    find_sokoball(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    coords(X1,Y1,Dir,X2,Y2),
    remove_sokoball(X,Y,State,DeletedSokoball),
    remove_box(X1,Y1,DeletedSokoball,DeletedSokoballAndBox),
    add_sokoball(X1,Y1,DeletedSokoballAndBox,AddedSok).
    add_box(X2,Y2,AddedSok,NewState).


# C


Initial State -> sokobal(13,3)
Utilizando por prioridade as direções N,S,W,E

Aplicando o BFS:

iter1:
State 1.1 North (1)

iter2 :
State 1.2 West (2)

iter3:
State 1.2.1 South(3)
State 1.2.2 West (4)

iter4: 
State 1.2.1.1 South(5)
State 1.2.2.1 South(6)
State 1.2.2.2 West(7)

iter5:
State 1.2.1.1.1 South(8)
State 1.2.2.2.1 West(9)

iter5:
State 1.2.2.2.1.1 North(10) Acaba no interruptor.


A utilização de um algoritmo como o A* sem heuristica(h = 0) é o mesmo que o BFS, agora com heuristica, sendo esta por exemplo a distancia de ManHattan de cada caixa a uma das portas iria melhorar a procura.






