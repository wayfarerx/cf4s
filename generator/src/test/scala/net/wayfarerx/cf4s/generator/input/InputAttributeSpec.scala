/* InputAttributeSpec.scala
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
 * Test suite for input attributes.
 */
object InputAttributeSpec extends ZIOSpecDefault:

  /** The tests that validate input attributes. */
  override def spec: Spec[Any, Nothing] = suite(classOf[InputAttribute].getName)(

    test("Decodes sequences of input attributes.") {
      val json = JsonObject(
        "First" -> JsonObject(
          `PrimitiveType` -> `String`.asJson
        ).asJson,
        "Second" -> JsonObject(
          `PrimitiveType` -> `Long`.asJson
        ).asJson
      ).asJson
      assertTrue(InputAttribute.decodeAll(json.hcursor) == Right(Seq(
        InputAttribute("First", InputType.String),
        InputAttribute("Second", InputType.Long)
      )))
    }

  )
