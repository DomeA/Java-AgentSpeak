hallo(123).
second(true).

+!drive
    <-
        generic/print(Score, Cycle);

        [A|B|C] = collection/list/range(1, 6);

        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print(A,B,C,  O,P,H,I);

        X = true;
        X = !X;
        generic/print(X);

        Z = 4 ** 0.5;
        Z = 100 * Z;
        generic/print(Z);

        // sequencial lambda expression
        // (O) -> Y | generic/print(Y)
        // parallel lambda expression
        // @(O) -> Y | { generic/print(Y); generic/print(O); }

        .