# FindAll predicate


- age(Pedro,15)

- age(Maria,13)

- age(Afonso,7)

- age(Tomas,10)

### findall(Name,age(Name,Age),List).
List = [Pedro,Maria,Afonso,Tomas].

### findall(Age,age(Name,Age),List).
List = [15,13,7,10].