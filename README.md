# x.equals

It compares two cases classes excluding specific *field names* rather than types.

## Example:
```
import equalsIgnoringFields._
import equalsIgnoringFields.syntax._

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
    count = 2
  )

val buterfliesStation2 = Buterflies(
    _id = 2,
    date = 1132,
    count = 2
  )

assert(buterfliesStation1.equalsIgnoringFields('_id, 'count)(buterfliesStation2))
assert(buterfliesStation1 != buterfliesStation2)

```
