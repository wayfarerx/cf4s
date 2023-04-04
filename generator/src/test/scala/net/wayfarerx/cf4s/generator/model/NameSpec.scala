/* NameSpec.scala
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

import zio.test.*

/**
 * Test suite for names.
 */
object NameSpec extends ZIOSpecDefault:

  /** The tests that validate names. */
  override def spec: Spec[Any, Throwable] = suite(classOf[Name].getName)(

    test("Represents strings that are valid names.") {
      for
        token <- Token fromString "Three"
        id <- Id fromString "One::Two"
        name1 <- Name fromString "Three"
        name2 <- Name fromString "One::Two.Three"
        result <- assertTrue(name1 == Name(None, token)) &&
          assertTrue(name1.toString == "Three") &&
          assertTrue(name2 == Name(Some(id), token)) &&
          assertTrue(name2.toString == "One::Two.Three")
      yield result
    }

  )
