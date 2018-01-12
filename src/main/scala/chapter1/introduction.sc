final case class Cat(name: String, age: Int, color: String)

trait Printable[A] { //THIS IS THE "TYPECLASS"
  def format(x: A): String
}

object PrintableInstaces { // "TypeClass instances"
  implicit val intPrinter: Printable[Int] = (x: Int) => x.toString
  implicit val stringPrinter: Printable[String] = (x: String) => x.toUpperCase
  implicit val catPrinter: Printable[Cat] = (x: Cat) => s"${x.name} is a ${x.age} year old ${x.color} cat"
}

object Printable {
  def format[A](x: A)(implicit printable: Printable[A]): String = printable.format(x)
  def print[A](x: A)(implicit printable: Printable[A]): Unit = println(printable.format(x))
}

object PrintableSyntax { // This is just for nice syntax
  implicit class PrintableOps[A] (a: A) {
    def format(implicit printable: Printable[A]): String = printable.format(a)
    def print(implicit printable: Printable[A]): Unit = Printable.print(a)
  }
}

import PrintableInstaces._
import PrintableSyntax._

Printable.print(3)
Printable.format(3)

val cat = Cat("hans", 5, "blau")
Printable.print(cat)

cat.format
3.print