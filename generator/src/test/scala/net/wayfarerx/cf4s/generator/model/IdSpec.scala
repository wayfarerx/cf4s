/* IdSpec.scala
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
package model

import cats.data.NonEmptySeq
import zio.test.*

/**
 * Test suite for IDs.
 */
object IdSpec extends ZIOSpecDefault:

  /** The tests that validate IDs. */
  override def spec: Spec[Any, Throwable] = suite(classOf[Id].getName)(

    test("Represents strings that are valid IDs.") {
      for
        token1 <- Token fromString "One"
        token2 <- Token fromString "Two"
        id1 <- Id fromString "One"
        id2 <- Id fromString "One::Two"
        result <- assertTrue(id1 == Id(NonEmptySeq.one(token1))) &&
          assertTrue(id1.toString == "One") &&
          assertTrue(id2 == Id(NonEmptySeq.of(token1, token2))) &&
          assertTrue(id2.toString == "One::Two")
      yield result
    }

  )
