# equals-ignoring-fields

It compares two cases classes excluding specific *field names* rather than types.

## Getting started

```scala
libraryDependencies += "ai.x" %% "equals-ignoring-fields" % "1.0.0"
```

## Example:
```scala
import ai.x.equalsIgnoringFields._
import ai.x.equalsIgnoringFields.syntax._

sealed trait Monarch

case class Butterflies(
  _id: Long,
  date: Long,
  count: Int
) extends Monarch

case class Dictator(
  _id: Long,
  date: Long,
  count: Int
) extends Monarch

val butterfliesStation1 = Butterflies(
      _id = 1,
      date = 1131,
      count = 3
    )
val butterfliesStation2 = Butterflies(
      _id = 2,
      date = 1131,
      count = 2
    )

assert(butterfliesStation1.equalsIgnoringFields('_id, 'count)(butterfliesStation2)) // the two objects are the same if we ignore those two fields
assert(!butterfliesStation1.equalsIgnoringFields('_id)(butterfliesStation2)) // the two objects are different if not ignoring `count`
assert(butterfliesStation1 != butterfliesStation2) // the two objects are different, period

```
