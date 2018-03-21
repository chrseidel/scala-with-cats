# Monoids

* associativity (a+(b+c) = (a+b)+c) required
* commutativity (a∘b = a∘b) not required
* requires identity function
* requires operation with set of elements (closed over the elements => A + A = A [example Int + Int = Int 👍] and not B + B = C [example Char + Char = String👎])

[𝕟,+) All natural numbers, without 0 on the + operation
* is not a monoid (identity (+0) is missing )
* is a semigroup (monoid without identity)

**semigroup** with identity is a **monoid**
monoid with inverse (a∘b = identity) is a **group**

```
def acc[T](ts: List[T])(implicit ev: Monoid[T]) = ts.foldLeft(ev.identity) { (acc, t) => acc.operation(t) }

trait Monoid[A] {
	def operation(l: A, r: A): A
	def empty: A
}

class IntAdditionMonoid extends Monoid[Int] {
	def operation(l: Int, r: Int) = l + r
	def empty = 0
}
```

> "Monad is a Monoid in the category of Endofunctors"
```

 Type A           Type B
 |-----| Functor |------|
 |  A  |========>| F[B] |
 |     |         |      | "Endofunctor" = functor from F[B] to F[B]
 |  B  |<========| F[A] |
 |-----|         |------|

```

map of List is a Functor
```
             //=> F[A] is the Type constructor
fmap(f: A=B)(F[A]): F[B]
```
