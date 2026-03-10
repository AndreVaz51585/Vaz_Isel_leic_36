ex1.


brother(X,Y) :- parent(Z,X), parent(Z,Y) , not(X=Y)

cousin(X,Y) :- parent(Z,X) , parent(W,Y), brother(Z,W)

grandson(X,Y) :- parent(Y,W) , parent(W,X)

descendent(X,Y) :- parent(Y,X) 
descendent(X,Y) :- parent(W,X), descendent(W,Y)






