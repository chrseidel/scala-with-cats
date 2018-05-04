package chapter4

trait Monad[M[_]] {
  def pure[A]: A => M[A]
  def flatMap[A,B](f: A => M[B]): M[A] => M[B]

  def map[A,B](f: A => B): M[A] => M[B] = ma => flatMap(f andThen pure)(ma)
}

//Monad Type
case class Writer[A](logs: Vector[String], value: A)

// Typeclass instances
object WriterMonadInstance extends Monad[Writer[Int]] {
  override def pure[A]: A => Writer[A] = a => Writer(Vector.empty, a)

  override def flatMap[A, B](f: A => Writer[B]): Writer[A] => Writer[B] = {
    case Writer(logs, value) =>
      val secondWriter = f(value)
      Writer(logs ++ secondWriter.logs, secondWriter.value)
  }
}

