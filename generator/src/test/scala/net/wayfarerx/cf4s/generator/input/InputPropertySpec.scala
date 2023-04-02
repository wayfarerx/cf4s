/* InputPropertySpec.scala
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
package input

import io.circe.JsonObject
import io.circe.syntax._
import zio.test._

/**
 * Test suite for input properties.
 */
object InputPropertySpec extends ZIOSpecDefault:

  /** The tests that validate input properties. */
  override def spec: Spec[Any, Nothing] = suite(classOf[InputProperty].getName)(

    test("Decodes sequences of input properties.") {
      val json = JsonObject(
        "First" -> JsonObject(
          `PrimitiveType` -> `String`.asJson,
          `Required` -> true.asJson,
          `Documentation` -> "first".asJson
        ).asJson,
        "Second" -> JsonObject(
          `Type` -> "Named".asJson,
          `Required` -> false.asJson,
          `Documentation` -> "second".asJson
        ).asJson
      ).asJson
      assertTrue(InputProperty.decodeAll(json.hcursor) == Right(Seq(
        InputProperty("First", InputType.String, true, "first"),
        InputProperty("Second", InputType.Named("Named"), false, "second")
      )))
    }

  )
