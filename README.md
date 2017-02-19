# equals-ignoring-fields

*MOVED* to https://github.com/xdotai/typeless

It compares two cases classes excluding specific *field names* rather than types.

## Getting started

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies += "ai.x" %% "equals-ignoring-fields" % "1.0.2"
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

// the two objects are the same if we ignore those two fields
assert(butterfliesStation1.equalsIgnoringFields(field => field == '_id || field == 'count)(butterfliesStation2)) 
// the two objects are different if not ignoring `count`
assert(!butterfliesStation1.equalsIgnoringFields(_ == '_id)(butterfliesStation2))
// the two objects are different, period
assert(butterfliesStation1 != butterfliesStation2) 

```
