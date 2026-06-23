# A 


position(Pos).
hero(hero).
skeleton(skeleton).
pellet(pellet).
at(hero/pellet,Position).
points(N).
objectiveDoor(position)

pellet(p1).
pellet(p2).
pellet(p3).

enoughPoints(State) :-
    member(points(P),State)
    P >= 200


enoughPoints representa a condição de ter pelo menos 200 pontos.




initialState = [
    at(hero,PosHero),
    at(skeleton,PosSkeleton),
    at(objectiveDoor,PosDoor),
    points(0),
    pellet(P1),
    pellet(P2),
    pellet(P3)
]


goal([
    finished
])


can(move(Hero,PosX,PosY), [at(Hero,PosX)], clear(PosY)) :-
    hero(Hero),
    Position(PosX),
    Position(PosY).



effects(move(Hero,PosX,PosY), [at(Hero,PosY), ~clear(PosY), clear(PosX), ~at(Hero,PosX)]).




can(eat(Hero,Pellet,PosX, PosY), [at(Hero,PosX),at(Pellet,PosY)]):-
    hero(Hero),
    pellet(Pellet)
    Position(PosX),
    Position(PosY).


effects(eat(Hero,Pellet,PosX, PosY), [, ~pellet(Pellet), ~at(Pellet,PosY), clear(PosY), points(+1)]).    



can(exit(Hero,Pos), [at(Hero,Pos), objetiveDoor(Pos), enoughPoints]):-
    hero(Hero),
    objectiveDoor(Pos),
    Position(Pos)


effects(exit(Hero,Pos), [finished]).


move(Hero,PosX,PosY)
eat(Hero,Pellet,PosX, PosY)
exit(Hero,Pos)

# B 


Plan = [move(hero,(15,7),(15,6)), eat(hero,pellet(16,6), (15,6), (16,6),...)]

Sim, existem tarefas que podem ser feitas em paralelo, nomeadamente a movimentação do skeleton.



# C

Em POP, existe um ameaça se tivermos o seguinte cenário : (A,P,B) em que a ação A produz a condição P necessária para realizar B, contudo caso exista uma ação C realizada entre A e B que desfaça P, então Constitui uma ameaça.
Para resolver este tipo de ameaça, o POP tem em conta duas tecnicas, o Promotion, quando B<C e portanto realizamos C depois de B, e o Demotion, quando C<A e portanto realizamos C antes de A.
Neste caso em concreto, não existe nenhuma ameaça porque as ações do esqueleto são independentes do hero, e portnao o hero consegue fazer qualquer coisa indepente do esqueleto.






