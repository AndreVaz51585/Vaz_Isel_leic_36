% Figure 1.8 The family program.

parent(pam, bob).   % Pam is a parent of Bob
parent(tom, bob).
parent(tom, liz).
parent(bob, ann).
parent(bob, pat).
parent(pat, jim).

female(pam). % Pam is female
male(tom).   % Tom is male
male(bob).
female(liz).
female(ann).
female(pat).
male(jim).

mother(X, Y) :- parent(X,Y), female(X).         % X is the mother of Y if

grandparent(X, Z) :- parent(X,W), parent(W,Z).    % X is a grandparent of Z if

sister(X, Y) :- parent(Z,X), parent(Z,Y), female(X), X\=Y.         % X is a sister of Y if

ancestor(X, Z) :- parent(X,Z).        
ancestor(X, Z) :- parent(X,Y) , ancestor(Y,Z).       % X is ancestor of Z if