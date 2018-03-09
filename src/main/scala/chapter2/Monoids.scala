package chapter2

object Monoids {

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]) =
      monoid
  }

  object BooleanOrMonoid extends Monoid[Boolean] {
    override def empty = false

    override def combine(x: Boolean, y: Boolean) = x || y
  }

  def foldMonoid[A](monoid: Monoid[A])(x: Seq[A]): A = x.foldLeft(monoid.empty)((acc, element) => monoid.combine(acc, element))

  def isAssociative[A](x: Seq[A])(monoid: Monoid[A]): Boolean = foldMonoid(monoid)(x) == foldMonoid(monoid)(x.reverse)

}