
# A

graspBanana(Monkey, Position).
pickup(Monkey, Object, Position/Base_Object).
putdown(Monkey, Object, Position/Base_Object).
climb(Monkey, Object).
walk(Monkey, PosX, PosY).


monkey(monkey)

position(left),
position(center),
position(right),

object(chair),
object(Banana),
object(Desk)      


on(Monkey/Object,Base_Object)
monkeyAs(Monkey,Object)
clear(Monkey)



can(graspBanana(Monkey, Position), [on(Monkey,Chair),on(Chair,Desk)],on(Desk,Floor),Position(Desk,Right)):-
    monkey(Monkey),
    position(Right),
    object(Chair),
    object(Desk).



add(graspBanana(Monkey, Position), [monkeyAs(Banana)]).

delete(graspBanana(Monkey, Position), [clear(Monkey)]).



can(pickup(Monkey, Object, Position/Base_Object), [clear(Monkey),onposition(Object,Position), onposition(monkey,Position)]):-
    monkey(Monkey),
    object(Object),
    position(Position).



add(pickup(Monkey, Object, Position/Base_Object), [monkeyAs(Object)]).

delete(pickup(Monkey, Object, Position/Base_Object), [clear(Monkey)]).



can(putdown(Monkey, Object, Position/Base_Object), [monkeyAs(Monkey,Object), on(Monkey,Position/Base_Object)]):-
    monkey(Monkey),
    object(Object),
    position(Position).

add(putdown(Monkey, Object, Position/Base_Object), [clear(Monkey)]).

delete(putdown(Monkey, Object, Position/Base_Object), [monkeyAs(Monkey,Object)]).

can(climb(Monkey, Object) , [onposition(Monkey,Object)]):-
    monkey(Monkey),
    object(Object).

add(climb(Monkey, Object), [on(Monkey,Object)]).

delete(climb(Monkey, Object), [onposition(Monkey,Object)]).


can(walk(Monkey,PosX,PosY), [onposition(Monkey,PosX)]):-
   monkey(Monkey),
   position(PosX). 


add(walk(Monkey,PosX,PosY), [onposition(Monkey,PosY)]).

delete(walk(Monkey,PosX,PosY), [onposition(Monkey,PosX)]).



#B

monkey(monkey1).
monkey(monkey2).


initialState = [at(monkey1,left), at(monkey2,right), at(table,left), at(chair,center), at(banana,right)]


Plan = [pickup(monkey1,table,left), walk(monkey2,right,center), pickup(monkey2,chair,center), walk(monkey1,left,right),putdown(monkey1,table,right), walk(monkey2,center,right), putdown(monkey2,chair,table),climb(monkey2,table),climb(monkey1,table),climb(monkey2,chair),climb(monkey1,chair),graspBanana(monkey2,right),graspBanana(monkey1,right)].





#C


initialState = [at(monkey,left), at(chair,center),at(table,right), at(banana,right)].

Goal = [at(chair,right), on(chair,floor)].


O plano gerado pelo planeador means ends tem somente em em conta um objetivo de cada vez, e portanto o objetivo que ele trabalha até ao fim é at(chair,right) e portanto o plano fica assim:

Plan = [walk(monkey,left,right), pickup(monkey,table,right), walk(monkey,right,left), putdown(monkey,table,left), walk(monkey,left,center), pickup(monkey,chair,center), walk(monkey,center,right),putdown(monkey,chair,right)].



# D

Constitui uma ameaça se por exemplo tivermos (A,P,B), ou seja, A produz a condição P necessária para realizar B, e portanto neste cenário, se um dos macacos realizar a ação C que por sua vez desfaz P então resulta numa ameaça, existem duas formas de combater isto, com promotion caso B<C e portanto só realizamos C depois de B, ou demotion quando C<A e portanto realizamos C antes de A.







