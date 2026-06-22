# Aspetos importantes

conc(L1,L2,L3) ou append(L1,L2,L3)

c1 : append([],L2,L2)

c2 : append(L1,[],L1)

c3: append([Head|T],L2,[H|TAIL]) :-
        append(L2,T,Tail)


Ex1.

