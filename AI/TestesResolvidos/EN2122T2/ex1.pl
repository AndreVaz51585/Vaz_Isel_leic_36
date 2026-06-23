
# A

Portanto vamos precisar de uma localização (x,y) para o hero, vamos precisar de uma localização (x,y) para cada pellet existente, vamos precisar também das localizações das duas portas, da magic potion, a trap e a porta objetivo.
As paredes por serem imutavéis podem ser consideradas por outro predicado.
O Hero pode mover-se nas direções N,S,O,E. 
E portanto os dois predicados principais seriam:
move(Hero,Dir), sendo Dir todas as direções possiveis e eat(Hero,Pellet,Dir).


Initial State = [hero(15,7), points(0), hasPotion(no), doors_broken([])] contendo todas as pellets que ainda não foram consumidas 

Final State = [hero(X,Y), points(P), objetiveDoor(X,Y)] , P >=200






# B 


s(State,NewState):-
    direction(Dir),
    apply_move(State,Dir,NewState).


direction(n).
direction(s).
direction(o).
direction(e).


coords(X,Y,Dir,X1,Y1).

coords(X,Y,n,X1,Y1) :-
  Y1 is Y - 1   


coords(X,Y,s,X1,Y1) :-
  Y1 is Y + 1


coords(X,Y,e,X1,Y1) :-
  X1 is X + 1  


coords(X,Y,o,X1,Y1) :-
  X1 is X - 1


walls([wall(..,..)]).

wall(X,Y).
pellet(X,Y).


pellet([pellet()])


walls_at(X,Y):-
    walls(Walls),
    member(wall(X,Y),Walls).

pellet_at(X,Y):-
    pellet(Pellets),
    member(pellet(X,Y),Pellets).



delete(X, [X|L], L).

delete(X, [H|R], [H|L]):-
    delete(X,R,L).


add(X, L1, [X|L1]).



remove_hero(State,NewState):-
    find_hero(State,X,Y),
    delete(hero(X,Y), State, NewState).


remove_pellet(X,Y,State,NewState):-
    delete(pellet(X,Y), State, NewState).

add_hero(State,X,Y,NewState):-
    add(hero(X,Y),State,NewState).


find_hero(State,X,Y):-
    member(hero(X,Y),State).
    

trap(X,Y).


can_move_hero(State,Dir):-
   find_hero(State,X,Y),
   coords(X,Y,Dir,X1,Y1),
   \+ walls_at(X1,Y1). 




apply_move(State,Dir,NewState):-
    can_move_hero(State,Dir),
    find_hero(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    \+ pellet_at(X1,Y1),
    remove_hero(State,NoHero),
    add_hero(NoHero,X1,Y1,NewState).


apply_move(State,Dir,NewState):-
    can_move_hero(State,Dir),
    find_hero(State,X,Y),
    coords(X,Y,Dir,X1,Y1),
    pellet_at(X1,Y1),
    add_point(State,AddedPoint),
    remove_pellet(X1,Y1,AddedPoint,Remove_pellet),
    remove_hero(Remove_pellet,Removed_heroAndPellet),
    add_hero(Removed_heroAndPellet,X1,Y1,NewState).


points(N).

objetiveDoor(X,Y).

is_finish(State):-
    get_points(State,Points),
    Points >= 200,
    find_hero(State,X,Y),
    objetiveDoor(X,Y).



get_points(State,Points):-
    member(points(Points),State).


add_point(State,NewState):-
    get_points(State,Points)
    P is Points + 1,
    delete(points(Points),State,DelState),
    add(points(P),DelState,NewState).




# C


hero(15,7).

Directions N,S,O,E.


BFS:

iter 1:

BFS :

Iter 1: 

[N, O] -> Pesquisas N [O]

[O,N,E]

State 1.1 Norte (1) (15,6)
State 1.2 Oeste (2) (14,7)

[N,O,N,O,E,S,O]

State 1.1.1 Norte (3 ) (15,5)
State 1.1.2 Este (4) (16,6)
State 1.2.1 Sul (5) (14,8)
State 1.2.2 Oeste (6) (13,7)
State 1.1.1.1 Norte (7) (15,4)
State 1.1.2.1 Este (8) (17,6)
State 1.2.1.1 Sul (9) (14,9)
State 1.2.2.1 Norte(10) (13,6)




DFS ID :

Iter 1 :

State 1.1 Norte (1)
State 1.2 Oeste (2)

Iter 2 : 
State 1.1.1 Norte(3)
State 1.1.2 Este (4)
State 1.2.1 Sul (5)
State 1.2.2 Oeste (6)

Iter 3 : 
State 1.1.1.1 Norte(7)
State 1.1.2.1 Este(8)
State 1.2.1.1 Sul(9)
State 1.2.1.2 Norte(10)













