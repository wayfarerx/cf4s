/* InputResourceTypeSpec.scala
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

package net.wayfarerx.cf4s.generator.input

import io.circe.JsonObject
import io.circe.syntax._
import zio.test._

/**
 * Test suite for input resource types.
 */
object InputResourceTypeSpec extends ZIOSpecDefault:

  /** The tests that validate input resource types. */
  override def spec: Spec[Any, Nothing] = suite(classOf[InputResourceType].getName)(

    test("Decodes sequences of input resource types.") {
      val json = JsonObject(
        "First" -> JsonObject(
          `Documentation` -> "first".asJson,
          `Attribute` -> JsonObject(
            "A1" -> JsonObject(
              `PrimitiveType` -> `Integer`.asJson
            ).asJson
          ).asJson,
          `Attributes` -> JsonObject(
            "A2" -> JsonObject(
              `PrimitiveType` -> `Long`.asJson
            ).asJson
          ).asJson,
          `Property` -> JsonObject(
            "P1" -> JsonObject(
              `PrimitiveType` -> `Double`.asJson,
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
          `Attributes` -> JsonObject(
            "A3" -> JsonObject(
              `PrimitiveType` -> `Json`.asJson
            ).asJson
          ).asJson,
          `Properties` -> JsonObject(
            "P3" -> JsonObject(
              `PrimitiveType` -> `Timestamp`.asJson,
              `Required` -> false.asJson,
              `Documentation` -> "p3".asJson
            ).asJson
          ).asJson
        ).asJson
      ).asJson
      assertTrue(InputResourceType.decodeAll(json.hcursor) == Right(Seq(
        InputResourceType("First", "first",
          Seq(InputAttribute("A1", InputType.Integer), InputAttribute("A2", InputType.Long)),
          Seq(InputProperty("P1", InputType.Double, true, "p1"), InputProperty("P2", InputType.String, true, "p2"))
        ),
        InputResourceType("Second", "second",
          Seq(InputAttribute("A3", InputType.Json)),
          Seq(InputProperty("P3", InputType.Timestamp, false, "p3"))
        )
      )))
    }

  )
