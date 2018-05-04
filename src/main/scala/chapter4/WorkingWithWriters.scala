package chapter4

import cats.data.Writer

import scala.collection.immutable

object WorkingWithWriters {

  import cats.syntax.writer._
  import cats.syntax.applicative._
  import cats.instances.vector._

  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body: => A): A = try body finally Thread.sleep(100)

  def factorialWithFlatMap(n: Int): Logged[Int] = {
    slowly(if (n == 0) 1.pure[Logged] else factorialWithFlatMap(n-1) map (_ * n)) // calc value
      .flatMap(a => Vector(s"fact $n $a").tell map { _ => a}) // add log and yield value
  }

  def factorialWithForComprehension(n: Int): Logged[Int] = for {
    ans <- slowly(if (n == 0) 1.pure[Logged] else factorialWithForComprehension(n -1) map (_ * n))
    _ <- Vector(s"fact $n $ans").tell
  } yield ans

  def myFactorial(n: Int): WriterMonadInstance[]

  def main(args: Array[String]): Unit = {
    import scala.concurrent._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    Await.result(Future.sequence(Vector(
      Future(factorialWithFlatMap(3).run),
      Future(factorialWithForComprehension(3).run)
    )), 5.seconds).foreach { case (vec, i) =>
      println(s"$i ==> $vec");
    }
  }
}
