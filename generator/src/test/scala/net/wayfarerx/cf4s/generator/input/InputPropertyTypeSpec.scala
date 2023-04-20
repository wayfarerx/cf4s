/* InputPropertyTypeSpec.scala
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
import io.circe.syntax.*

import zio.test.*

/**
 * Test suite for input property types.
 */
object InputPropertyTypeSpec extends ZIOSpecDefault:

  /** The tests that validate input property types. */
  override def spec: Spec[Any, Nothing] = suite(classOf[InputPropertyType].getName)(

    test("Decodes sequences of input property types.") {
      val json = JsonObject(
        "First" -> JsonObject(
          `Documentation` -> "first".asJson,
          `Property` -> JsonObject(
            "P1" -> JsonObject(
              `PrimitiveType` -> `Long`.asJson,
              `Required` -> true.asJson,
              `Documentation` -> "p1".asJson
            ).asJson
          ).asJson,
          `Properties` -> JsonObject(
            "P2" -> JsonObject(
              `PrimitiveType` -> `String`.asJson,
              `Required` -> true.asJson,
              `Documentation` -> "p2".asJson
            ).asJson
          ).asJson
        ).asJson,
        "Second" -> JsonObject(
          `Documentation` -> "second".asJson,
          `Properties` -> JsonObject(
            "P3" -> JsonObject(
              `PrimitiveType` -> `Timestamp`.asJson,
              `Required` -> false.asJson,
              `Documentation` -> "p3".asJson
            ).asJson
          ).asJson
        ).asJson
      ).asJson
      assertTrue(InputPropertyType.decodeAll(json.hcursor) == Right(Seq(
        InputPropertyType("First", "first", Seq(
          InputProperty("P1", InputType.Long, true, "p1"),
          InputProperty("P2", InputType.String, true, "p2")
        )),
        InputPropertyType("Second", "second", Seq(
          InputProperty("P3", InputType.Timestamp, false, "p3")
        ))
      )))
    }

  )
