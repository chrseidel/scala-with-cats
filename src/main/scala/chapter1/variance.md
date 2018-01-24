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