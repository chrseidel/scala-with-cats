package chapter1

import cats.instances.string._
import cats.instances.int._
import cats.instances.option._
import cats.syntax.eq._
import cats.Eq

object Equality {

  implicit val catEq = Eq.instance[Cat] { (catA, catB) =>
    catA.name === catB.name && catA.age === catB.age && catA.color === catB.color
  }

  def main(args: Array[String]): Unit = {
    val cat1 = Cat("Garfield",   38, "orange and black")
    val cat2 = Cat("Heathcliff", 33, "orange and black")
    val optionCat1 = Option(cat1)
    val optionCat2 = Option.empty[Cat]

    println(cat1 === cat1)
    println(cat1 === cat2)
    println(optionCat1 === optionCat1)
    println(optionCat1 === optionCat2)
    println(optionCat1 =!= optionCat2)
  }
}