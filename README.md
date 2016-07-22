# equals-ignoring-fields

It compares two cases classes excluding specific *field names* rather than types.

## Getting started

For now the artifact is deployed in our artifactory, when this repo becomes public will go to sonatype

#### Server

```
"xdotai release"  at "http://artifactory.xdotai-internal.net:8081/artifactory/libs-release-local",
```

```
libraryDependencies += "ai.x" %% "equals-ignoring-fields" % "1.0.0"
```

## Example:
```scala
import ai.x.equalsIgnoringFields._
import ai.x.equalsIgnoringFields.syntax._

sealed trait Monarch

case class Buterflies(
  _id: Long,
  date: Long,
  count: Int
) extends Monarch

case class Dictator(
  _id: Long,
  date: Long,
  count: Int
) extends Monarch

val buterfliesStation1 = Buterflies(
      _id = 1,
      date = 1131,
      count = 3
    )
val buterfliesStation2 = Buterflies(
      _id = 2,
      date = 1131,
      count = 2
    )

assert(buterfliesStation1.equalsIgnoringFields('_id, 'count)(buterfliesStation2)) // the two objects are the same if we ignore those two fields
assert(!buterfliesStation1.equalsIgnoringFields('_id)(buterfliesStation2)) // the two objects are different if not ignoring `count`
assert(buterfliesStation1 != buterfliesStation2) // the two objects are different, period

```
