member(X,[X|Tail]).  % ou seja, X é um membro de uma lista se X é a cabeça da lista

member(X,[Head|Tail]) :- member(X,Tail).  % ou seja, X é um membro de uma lista se X é um membro da cauda da lista

append([], L2, L2). % lista vazia concatenada com L2 é L2

append(L1, [], L1). % L1 concatenada com lista vazia é L1

append([H | T], L2, [H | Tail]) :- % ou seja, a concatenação de uma lista com cabeça H e cauda T com L2 é uma lista com cabeça H e cauda Tail, se Tail é a concatenação de T com L2
	append(T, L2, Tail).


head([H | Tail], H).

second([H, S | Tail], S).

third([H, S, T | Tail], T).

% eliminar um determinado elemento de uma lista

delete(X,[X|Tail], Tail).

delete(X,[Head|Tail],[Head|T]) :- delete(X,Tail,T). 


% adicionar um elemento à cabeça de uma lista

addHead(X,L,[X|L]).