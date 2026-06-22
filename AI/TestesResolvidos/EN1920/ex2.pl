# A

pickup(Rob, Object, Position).

putdown(Rob, Object, Position).

stack(Rob, Base_Object, Object).

unstack(Rob, Object, Base_Object).




Definition of act pickup:

can(Action, Condition): Action is only possible if Condition


can(pickup(Rob,Object,Position), [clear(Rob), Clear(Object), on(Object, Position)]):-
    robot(Rob),
    place(Position),
    block(Object).


adds(pickup(Rob,Object,Position), [robotHas(Rob,object), clear(Position),]).

deletes(pickup(rob,Object,Position), [clear(Rob), on(Object,Position),]).





Definition of act putdown():

can(putdown(Rob,Object,Position), [robotHas(Rob,Object), clear(Position)]):-
    robot(Rob),
    place(Position),
    block(Object).



adds(putdown(Rob,Object,Position), [clear(Rob), on(Object,Position)]).

deletes(putdown(Rob,Object,Position), [robotHas(Rob,Object), clear(Position)]).


Definition of act stack(Rob,Base_Object,Object):

can(stack(Rob,Base_Object,Object), [robotHas(Rob,Object), clear(Base_Object)]):-
    robot(Rob),
    block(Base_Object),
    block(Object).


add(stack(Rob,Base_Object,Object), [clear(Rob), on(Object,Base_Object)]).    

deletes(stack(Rob,Base_Object,Object), [robotHas(Rob,Object), clear(Base_Object)]).    



Definition of unstack(Rob, Object, Base_Object);


can(unstack(Rob, Object, Base_Object), [clear(Rob),clear(Object), on(Object,Base_Object)]):-
    robot(Rob),
    block(Base_Object),
    block(Object).


add(unstack(Rob, Object, Base_Object), [robotHas(Rob,Object), clear(Base_Object)]).    

deletes(unstack(Rob, Object, Base_Object), [on(object,Base_Object), clear(Rob)]).    


block(a).
block(b).
block(c).
block(d).
block(e).
block(f).

place(1).
place(2).
place(3).
place(4).
place(5).
place(6).
place(7).
place(8).

robot(rob1).
robot(rob2).


#B

Começamos por definir o estado inicial:
 
state1([clear(rob1),clear(rob2), clear(3), clear(4), clear(5), clear(6), clear(b), clear(c),clear(f),clear(e),
 on(a,1),on(b,a), on(c,2), on(f,7), on(d,8), on(e,d)]).

Depois usamos novamente o estado inicial e passamos o plan() que contém o estado objetivo

state1(Start), plan(Start, [on(a,b),on(b,c),on(d,e), on(e,f)], Plan, FinState).

Depois definimos o plano para atingir o objetivo:

Plan = [unstack(rob1,b,a), stack(rob1,c,b),pickup(rob1,a,1), stack(rob1,b,a),
unstack(rob2, e, d), stack(rob2, f, e), pickup(rob2, d, 8), stack(rob2, e, d) ]

FinState = [clear(rob1), on(a, b), clear(1), on(b, c), clear(a), clear(3), clear(4), on(c, 2)]

Então e o segundo estado???? 




#C

state1(Start), plan(Start, [on(a, 2), clear(a)], Plan, FinState).

Start = [clear(rob1), clear(3), clear(4), clear(b), clear(c), on(a, 1), on(b, a), on(c, 2)],

Plan = [ unstack(rob1,b,a), putdown(rob1,b,3), pickup(rob1,c,2), putdown(rob1,c,4), pickup(rob1,a,1), putdown(rob1,a,2) ]

FinState = [clear(rob1) , clear(1), on(a,2), on(b,3), on(c,4), clear(a)clear(c),clear(b)]

Explicação de execução do means~ends:
Sem Goal regression, nós vamos pegar no objetivo e vamos até ao fim para satisfazer esse objetivo, somente quando esse objetivo estiver satisfeito, é que passamos ao proximo,
o que muitas vezes acaba por gerar planos suboptimos isto porque uma vez que não temos em conta o conjunto completo dos goals a cada iteração, podemos realizar ações que desfazem outros goals no ambito de satisfazer o primeiro etc

#D

o predicado causa (A,P,B) que significa, que A produz o condition/Goal P, necessário pela ação B.

Se existir uma ação C que destroi o goal P no intervalo entre A e B, então C deve ser realizado antes de A ou depois de B.

Exemplo:

rob1 produz clear(a) necessário para realizar a ação pickup(rob1,a,1), e depois o rob2 executa (stacks,a,e) o que destroi o clear()