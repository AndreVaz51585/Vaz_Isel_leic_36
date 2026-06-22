# A 

List of adjacent positions form the map:
adjacent(1/1, 2/1).
adjacent(2/1, 3/1).
adjacent(3/1, 4/1).
adjacent(4/1, 5/1).
adjacent(5/1, 6/1).
adjacent(6/1, 7/1).
adjacent(7/1, 8/1).
adjacent(8/1, 9/1).
adjacent(9/1, 10/1).




State components:

pacmanAt(PacPos)
pelletAt(PelletPos)
clear(Pos)

pacman(1)
pacman(2)

pelletAt(1/1)
pelletAt(1/2)

can(move(Pac,Pos1,Pos2), [pacmanAt(Pac,Pos1), clear(Pos2)]):-
   pacman(Pac),
   adjacent(Pos1,Pos2).



O effects corresponde ao que é verdade e não é verdade depois de aplicada a movimentação

effects(move(Pac,Pos1,Pos2), [pacmanAt(Pac,Pos2), ~clear(Pos2), clear(Pos1), ~pacmanAt(Pac,Pos1)])



can(eat(Pac,Pos1,Pos2), [on(Pac,Pos1), pellet_at(Pos2)]) :-
    pacman(Pac),
    adjacent(Pos1,Pos2)


effects(eat(Pac,Pos1,Po2), [on(Pac,Pos2), ~pellet_at(Pos2), clear(Pos1)], ~pacmanAt(Pac,Pos1))

Initial State : 

[pacmanAt(1,1/30), pacmanAt(2,14/30), pellet_at(X1,Y2), ....]

Final State: [pacmanAt(1, 1/30), pacmanAt(2, 26/30), pelletAt(X1/Y2), ... excluding the bottom line where there exist clear(X1/Y1) predicates except for the goal positions]


#B 

Actions for moving pacman 1 towards its goal:
eat(1, 13/30, 12/30),
...
eat(1, 2/30, 1/30)

Actions for moving pacman 2 towards its goal:
eat(2, 14/30, 15/30),
...
eat(2, 25/30, 26/30)

Each pair of actions (eat(1, 13/30, 12/30) and eat(2, 14/30, 15/30)), one from each subplan, can be done simultaneously by the POP planner.

# C

Costuma existir problemas em POP, quando temos por exemplo (A,P,B) em que a ação A produz a condição P necessária para realizar B, isto costuma constituir uma threat quando existe uma ação que desfaz P impedindo B de se concretizar. 
Deste modo, uma vez que temos dois pacmans e 1) não é possivel estarem na mesma posição ao mesmo tempo, 2) eles movimentam-se em torno de pellets e portanto quando a pellet qeu eles estão a movimentar fica clear passam para outra e portanto não existe qualquer threat.


