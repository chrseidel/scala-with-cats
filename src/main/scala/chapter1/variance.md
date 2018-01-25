# Variance
*From https://medium.com/@sinisalouc/variance-in-java-and-scala-63af925d21dc*

* **Covariance** A <: B then also T[A] <: T[B]
* **Contravariance** = A <: B then T[A] :> T[B]
* **Invariance** = Relationship of A to B has no meaning to T

In Java, arrays are *covariant*.

```
Object[] objects;
objects = new Integer[10];
objects[1] = "dafuq";
// compiles, but explodes at runtime :(
// Exception in thread "main" java.lang.ArrayStoreException: java.lang.String
```
compiler thinks, this is an Array of Objects, but is actually array of Integers.

## Generics to the rescue:

`MyClass<String> not subclass of MyClass<Object>`
Type tags are **not covariant**

This means, if a method requires a `MyClass<Object>`, I *can not* pass a `MyClass<String>`.

## **Bounds** to the rescue
```
public void process(List<? extends Car> list) { ... }
```
process accepts a List of Car *or any subtype of Car*. ==> **covariance**

```
public void process(List<? super Car> list) { ... }
```
or process could accept a List of Car *or all supertypes of Car*. ==> **contravariance**


```
MyClass<T extends Car> // also true for Type parameters, but uses T, instead of ?
MyClass<T super Bird> // does not compile :(
```
```
MyClass<T extends Bird & CanSwin & CanRun> // multiple interfaces => works! yey (But only for Type parameters!)
```
## General rule
* *covariant* type parameter as *return type*
* *contravariant* type parameter as in *input type*

### Example
`List<? extends Car>` is covariant / upper bounded\
What we get can only be a Car. We could safely(only compiler safe, could blow up at runtime) put this into a `List<Car>`\

`List<? super Car>` is contravariant / lower bounded\
We can put all Cars into the List. Even subtypes. All Subtypes of Cars, are also Cars.

```
List<? extends Integer> upperBounded = new ArrayList<>(); // covariant
List<? super Integer> lowerBounded = new ArrayList<>(); // contravariant

upperBounded.add(3); // doesn't compile
upperBounded.add(null); // works (null is subtype of everything)
lowerBounded.add(3); // works

Integer a = upperBounded.get(0); // works
Integer b = lowerBounded.get(0); // doens't compile
Object c = lowerBounded.get(0); // works
```
* We can only *get* stuff from *covariant* list
* We can only *put* stuff into *contravariant* list
This is called the **get-put-principle**
* use **covariance** for methods which **return** a generic type
* use **contravariance** for methods which **take** a generic type
* use **invariance** for methods which both accept and return a generic type

## Scala
In Scala
* Arrays are **invariant**
* Lists are **covariant** by default (safe because of immutability)

This is not really an advantage.\
The real advantage is, that in Java you *have to* use **use-site variance**, which means, you declare variance where the type-parameter is *used*.\
Scala supports use-site variance as well: `MyClass[_ <: T]`.\
Scala also supports **declaration-site variance**: `MyClass[+T]`\
* **+** for *covariance*
* **-** for *contravariance*

### Example Java vs. Scala
```
// in Java
public class Foo<T> { ... }
...
Foo<? extends Integer> covariantFoo = new Foo<Integer>();
Foo<? super Integer> contravariantFoo = new Foo<Integer>();
```
The variance is defined, where it's *used*
```
// in Scala
class CovariantFoo[+T] { ... }
class ContravariantFoo[-T] { ... }
...
val covariantFoo = new CovariantFoo[Int]()
val contravariantFoo = new ContravariantFoo[Int]()
```
The variance is defined, where the classes Type-parameter is declared.\

None of the two approaches is *generally* "better". Declaration-site variance could be seen as an advantage, as it takes the reasoning about the variance off the user of the class/type.\
The Scala compiler will double check, if you're using declaration-site variance, if you're breaking the **rules of covariance**:
* **covariant** types *only* serve as **return types**
* **contravariant** types only serve as **method parameters**

### Variance in Scala
1st: Remember, that in Scala Functions are first-class citizens. Functions can have subclasses and superclasses (or subfunctions and superfunctions)\

```
trait Function1[-S, +T] { def apply(x: S): T }
```
This is the standard definition of the Function trait. It's contravariant in S and covariant in T.\
*Liskov substitution principle*:
> T is a subtype of U if it supports the same operations as U and all of its operations **require less** (or same) and **provide more** (or same) than the corresponding operations in U (subtyping is reflexive so S <: S)

* **Require less** means *more general*: Vehicle is less than a Car
* **Provide more** means *more specific*: Vehicle is more specific than AnyRef

`def getCarInfo: Car => AnyRef` is a supertype of
* `Car => AnyRef`
* `Vehicle => AnyRef`
* `Car => String`
* `Vehicle => String`

> If I want function B to be a subtype of function A, then B’s input parameter must be a superclass of A’s input parameter (contravariance) and B’s return value must be a subclass of A’s return value (covariance).

```
/**
 * Remember! In Scala, every function that takes one argument 
 * is an instance of Function1 with signature:
 *
 * trait Function1[-T, +S] extends AnyRef
 */

class Vehicle(val owner: String)
class Car(owner: String) extends Vehicle(owner)

object Printer {

  val cars = List(new Car("john"), new Car("paul"))

  def printCarInfo(getCarInfo: Car => AnyRef) { // we can also pass subtypes of Car => AnyRef
    for (car <- cars) println(getCarInfo(car))
  }
}

object Customer extends App {

 val getOwnerInfo: (Vehicle => String) = _.owner // this is a subtype of Car => AnyRef, because Vehicle is a supertype of Car (contravariant in its parameter) and String is a Subtype of AnyRef (covariant in its return-type)
 
 Printer.printCarInfo(getOwnerInfo)
}
```