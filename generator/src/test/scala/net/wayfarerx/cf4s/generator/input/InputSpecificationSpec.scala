/* InputSpecificationSpec.scala
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
import io.circe.syntax.*

import zio.test.*

/**
 * Test suite for input specifications.
 */
object InputSpecificationSpec extends ZIOSpecDefault:

  /** The tests that validate input specifications. */
  override def spec: Spec[Any, Nothing] = suite(classOf[InputSpecification].getName)(

    test("Decodes input specifications.") {
      val json = JsonObject(
        `PropertyType` -> JsonObject(
          "PT1" -> JsonObject(
            `Documentation` -> "pt1".asJson,
            `Properties` -> JsonObject(
              "P1" -> JsonObject(
                `PrimitiveType` -> `Double`.asJson,
                `Required` -> true.asJson,
                `Documentation` -> "p1".asJson
              ).asJson
            ).asJson
          ).asJson
        ).asJson,
        `PropertyTypes` -> JsonObject(
          "PT2" -> JsonObject(
            `Documentation` -> "pt2".asJson,
            `Properties` -> JsonObject(
              "P2" -> JsonObject(
                `PrimitiveType` -> `Boolean`.asJson,
                `Required` -> true.asJson,
                `Documentation` -> "p2".asJson
              ).asJson
            ).asJson
          ).asJson
        ).asJson,
        `ResourceType` -> JsonObject(
          "RT1" -> JsonObject(
            `Documentation` -> "rt1".asJson,
            `Attributes` -> JsonObject(
              "A1" -> JsonObject(
                `PrimitiveType` -> `Integer`.asJson
              ).asJson
            ).asJson,
            `Properties` -> JsonObject(
              "P3" -> JsonObject(
                `PrimitiveType` -> `Timestamp`.asJson,
                `Required` -> true.asJson,
                `Documentation` -> "p3".asJson
              ).asJson
            ).asJson
          ).asJson
        ).asJson,
        `ResourceTypes` -> JsonObject(
          "RT2" -> JsonObject(
            `Documentation` -> "rt2".asJson,
            `Attributes` -> JsonObject(
              "A2" -> JsonObject(
                `PrimitiveType` -> `Long`.asJson
              ).asJson
            ).asJson,
            `Properties` -> JsonObject(
              "P4" -> JsonObject(
                `PrimitiveType` -> `String`.asJson,
                `Required` -> true.asJson,
                `Documentation` -> "p4".asJson
              ).asJson
            ).asJson
          ).asJson
        ).asJson
      ).asJson
      assertTrue(json.as[InputSpecification] == Right(
        InputSpecification(
          Seq(
            InputPropertyType("PT1", "pt1", Seq(InputProperty("P1", InputType.Double, true, "p1"))),
            InputPropertyType("PT2", "pt2", Seq(InputProperty("P2", InputType.Boolean, true, "p2")))
          ),
          Seq(
            InputResourceType("RT1", "rt1",
              Seq(InputAttribute("A1", InputType.Integer)),
              Seq(InputProperty("P3", InputType.Timestamp, true, "p3"))
            ),
            InputResourceType("RT2", "rt2",
              Seq(InputAttribute("A2", InputType.Long)),
              Seq(InputProperty("P4", InputType.String, true, "p4"))
            )
          )
        )
      ))
    }

  )
