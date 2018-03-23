# Functor variance

## Covariant functor
`A <: B ==> F[A] <: F[B]` if A is **subtype** of B, F[A] is **subtype** of F[B]
`A => B ==> F[A] => F[B]` if there is a function from **A to B**, there is a function from **F[A] to F[B]**

## Contravariant functor
`A <: B ==> F[A] >: F[B]` if A is a **subtype** of B, F[A] is a **supertype** of F[B]
`A <= B ==> F[A] => F[B]` if there is a function from **B to A**, there is a function from **F[A] to F[B]**

## Invariant functor
`A <: B and B <: A ==> F[A] <: F[B], F[B] <: F[A]` if A is a **subtype** of B **and** B is also a **subtype** of A (*which only makes sense, if it's ultimately the same type*), the F[A] is a **subtype** of F[B] and F[B] is a **subtype** of F[A]
`A <= B and B <= A ==> F[A] <= F[B], F[B] <= F[A]` if there is a function from **A to B** and a function from **B to A**, there is also a function from **F[A] to F[B]** and a function from **F[B] to F[A]**
> If A and B are *isomorph*, also F[A] and F[B] are so.
