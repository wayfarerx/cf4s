/* StateSpec.scala
 *
 * Copyright (c) 2023 wayfarerx (@x@wayfarerx.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.wayfarerx.cf4s.generator

import zio.ZIO
import zio.interop.catz.*
import zio.test.*

/**
 * Test suite for ZIO states.
 */
object StateSpec extends ZIOSpecDefault:

  /** The tests that validate ZIO states. */
  override def spec: Spec[Any, Throwable] = suite(classOf[State.type].getName)(

    test("Passes state data between calls") {
      val state: State[Int, Seq[String]] = {
        for
          a <- State succeed Seq("A")
          _ <- State.modifyF[Int](ZIO succeed _ + 1)
          b <- State lift ZIO.succeed(a :+ "B")
          _ <- State.modify[Int](_ + 1)
          c <- State.inspect[Int, Seq[String]](s => b :+ s.toString)
          _ <- State.modify[Int](_ + 1)
          d <- State.inspectF[Int, Seq[String]](s => ZIO succeed c :+ s.toString)
          _ <- State.modify[Int](_ + 1)
          e <- State.foldLeft[Int, String, Seq[String]](Seq("c", "d", "e"))(Seq.empty) { (o, i) =>
            for
              _ <- State.modify[Int](_ + 1)
            yield o :+ i.toUpperCase
          }
        yield d ++ e
      }
      for result <- state.run(0) yield
        assertTrue(result == 7 -> Seq("A", "B", "2", "3", "C", "D", "E"))
    },

    test("Fails when instructed to do so.") {
      val thrown = new RuntimeException
      for
        result <- State.fail(thrown).run(0).exit
      yield assert(result)(Assertion fails Assertion.equalTo(thrown))
    }

  )
