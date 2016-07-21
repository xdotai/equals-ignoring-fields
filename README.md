# equals-except

It compares two cases classes excluding specific *fields* rather than types.

## Example:
```
sealed trait Animal
case class Cat(name: String, fish: Int) extends Animal
case class Dog(name: String, bones: Int) extends Animal

val felix: Animal = Cat("Felix", 1)
val juan= Cat("juan", 1)

scala> felix.equalsExcept(juan, 'fish)
res0: Boolean = false

scala> felix.equalsExcept(juan, 'name)
res1: Boolean = true

```
