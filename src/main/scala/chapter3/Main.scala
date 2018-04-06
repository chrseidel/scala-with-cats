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

  def contramap[B](func: B => A): Printable[B] = (value: B) => self.format(func(value))
}

object InvariantFunctor {
  trait Codec[A] {
    def encode(value: A): String
    def decode(value: String): A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = {
      val self = this
      new Codec[B] {
        def encode(value: B): String = self.encode(enc(value))
        def decode(value: String): B = dec(self.decode(value))
      }
    }
  }

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)
}
