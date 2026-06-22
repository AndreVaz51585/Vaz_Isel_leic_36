# Basic Solve


## State space

    s(a, b).
    s(a, c).
    s(b, d).
    s(b, e).
    s(d, h).
    s(e, i).
    s(e, j).
    s(c, f).
    s(c, g).
    s(f, k).

    goal(j).

    goal(f).

## Implementation of depth-first strategy

    basic_solve(N, [N]) :-
        goal(N).


    basic_solve(N, [N | Sol1]) :-
        s(N,N1),
        basic_solve(N1, [N1 | Sol1])    


