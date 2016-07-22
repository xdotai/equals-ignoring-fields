/*
 * Copyright (c) 2015-6 x.ai inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.x

package tests

import org.scalatest._
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

class TestEquals extends FunSuite with Matchers {
  test("same class equals") {
    val buterfliesStation1 = Buterflies(
      _id = 1,
      date = 1131,
      count = 2
    )
    val buterfliesStation2 = Buterflies(
      _id = 2,
      date = 1131,
      count = 2
    )

    assert(buterfliesStation1.equalsIgnoringFields('_id)(buterfliesStation2))
    assert(buterfliesStation1 != buterfliesStation2)
  }
  test("same class equals but same type") {
    val buterfliesStation1: Monarch = Buterflies(
      _id = 1,
      date = 1131,
      count = 2
    )
    val buterfliesStation2 = Buterflies(
      _id = 2,
      date = 1131,
      count = 2
    )

    assert(buterfliesStation1.equalsIgnoringFields('_id)(buterfliesStation2))
    assert(buterfliesStation1 != buterfliesStation2)
  }

  test("discard several fields") {
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

    assert(buterfliesStation1.equalsIgnoringFields('_id, 'count)(buterfliesStation2))
    assert(!buterfliesStation1.equalsIgnoringFields('_id)(buterfliesStation2))
    assert(buterfliesStation1 != buterfliesStation2)
  }

  test("two classes are different") {
    val buterfliesStation: Monarch = Buterflies(
      _id = 1,
      date = 1131,
      count = 2
    )
    val dictatorUltra: Monarch = Dictator(
      _id = 2,
      date = 1131,
      count = 2
    )

    assert(!buterfliesStation.equalsIgnoringFields('_id)(dictatorUltra))
  }

  test("same class bad field") {
    val buterfliesStation1 = Buterflies(
      _id = 1,
      date = 1131,
      count = 2
    )
    val buterfliesStation2 = Buterflies(
      _id = 2,
      date = 1131,
      count = 2
    )

    assert(!buterfliesStation1.equalsIgnoringFields('_id2)(buterfliesStation2))
    assert(buterfliesStation1 != buterfliesStation2)
  }
}
