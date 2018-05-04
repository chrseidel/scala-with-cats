# Monads

## Monad needs pure & flatMap

```
trait Monad[M[_]] {
	def pure[A]: A => M[A]
	def flatMap[A,B](f: A => M[B]): M[A] => M[B]
}
```

## Monad Laws
* **Left identity** `pure(a).flatMap(f) == f(a)`
* **Right identity** `m.flatMap(pure) == m`
* **Associativity** `m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))`

## Monads are functors (pure & map)
```
trait Monad[M[_]] {
	def pure[A]: A => M[A]
	def flatMap[A,B](f: A => M[B]): M[A] => M[B]

	// we can define map by only using pure and flatMap
	def map[A,B](f: A => B): M[A] => M[B] = ma: M[A] => flatMap(f andThen pure)(ma)
}
```

## OptionMonad
```
implicit object OptionMonad extends Monad[Option] {
	def pure[A] = Option.apply
	def flatMap[A,B](f: A => Option[B]) = oa: Option[A] => oa match {
		case Some(a) => f(a)
		case None => None // Note here, that the first None is actually a None of A and the return value is a None of B
	}
}
```

## FutureMonad
```
implicit object FutureMonad extends Monad[Future] {
	def pure[A] = Future.apply
	def flatMap[A,B](f: A => Future[B]) = { fa: Future[A] => 
		val p = Promise[B]()
		fa.onComplete({
			case Success(a) => p.success(f(a))
			case Failure(t) => p.failure(t)
		})
		p.future
	}
}
```