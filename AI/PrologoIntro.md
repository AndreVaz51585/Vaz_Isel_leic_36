# Multiple Cases, basics and important concepts 


##  Verify if a certain atom is a member of a List

    C1 : member(X,[X|Tail])  % its the head/first element of the list

    C2 : member(X,[Head|Tail]) :- member(X,Tail). % its in the Tail


## Verify if a certain element is the First, Second or Third element of a List

**First**
    
    first([X|Tail],X).

**Second**

    second([X,S|Tail],S).

**Third** 

    third([F,S,T|Tail],T).


## Append or Conc an element to a List

**addLast** :  add an element to the end of a List

    C1 : addLast(X,[],[X]). 

    C2 : addLast(X,[Head|Tail],[Head|T]) :- addlast(X,Tail,T).


**addFirst** : add an element to the beggining of a List

    C1: addFirst(X,L1,[X | L1]).



## Remove or Delete an element from a List


    C1 : delete(X,[X|Tail,Tail]).

    C2: delete(X,[Head|Tail,[Head|T]]) :- delete(X,Tail,T).




## Sum all the elements on a List

    C1 : sumList([],0); % a soma de uma lista vazia é 0

    C2 : sumList([Head|Tail],S):-
                sumList(Tail,Rest) % resolve recursivamente os restantes elementos da lista
                S is Head + Rest    


## Length of a list

    C1 : lenght([],0).

    C2 : length([H|T],L) :-
            lenght(T,Rest)
            L is Rest + 1    
