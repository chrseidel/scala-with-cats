package chapter1

import cats.Show
import cats.implicits._

object CatShow {
  implicit val catShow: Show[Cat] = new Show[Cat] {
    override def show(t: Cat): String = s"${t.name} is a ${t.age} year old ${t.color} cat"
  }

  def main(args: Array[String]): Unit = {
    Cat("hans", 5, "red") show

  }
}