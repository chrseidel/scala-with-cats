package chapter3

import cats.Functor
import cats.syntax.functor._

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object Funky {

  implicit val TreeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value) => Leaf(f(value))
    }
  }

  def main(args: Array[String]): Unit = {

    val testTree: Tree[Int] = Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))

    println(testTree.map(_ * 2).map(_.toString))

  }
}

trait Printable[A] { self => // !!!!
  def format(value: A): String

  def contramap[B](func: B => A): Printable[B] =
    (value: B) => self.format(func(value))
}
