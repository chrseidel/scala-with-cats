package chapter2

object Main {
  def main(args: Array[String]): Unit = {
    List(true,true,true,false,false,false).permutations.foreach(permList => {
      println(Monoids.isAssociative[Boolean](permList)(Monoids.BooleanOrMonoid))
    })
  }
}
