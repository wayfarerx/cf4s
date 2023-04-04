/* InputTypeSpec.scala
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
 * Test suite for input types.
 */
object InputTypeSpec extends ZIOSpecDefault:

  /** The tests that validate the supported input types. */
  override def spec: Spec[Any, Nothing] = suite(classOf[InputType].getName)(

    test("Decodes primitive input types.") {
      assertTrue(decodePrimitive(`Boolean`) == Right(InputType.Boolean)) &&
        assertTrue(decodePrimitive(`Integer`) == Right(InputType.Integer)) &&
        assertTrue(decodePrimitive(`Long`) == Right(InputType.Long)) &&
        assertTrue(decodePrimitive(`Double`) == Right(InputType.Double)) &&
        assertTrue(decodePrimitive(`String`) == Right(InputType.String)) &&
        assertTrue(decodePrimitive(`Timestamp`) == Right(InputType.Timestamp)) &&
        assertTrue(decodePrimitive(`Json`) == Right(InputType.Json)) &&
        assertTrue(decodePrimitive("Invalid").isLeft)
    },

    test("Decodes complex input types.") {
      assertTrue(decodeComplex(`List`, Some(`Boolean`)) == Right(InputType.List(InputType.Boolean))) &&
        assertTrue(decodeComplex(`Map`, Some(`Integer`)) == Right(InputType.Map(InputType.Integer))) &&
        assertTrue(decodeComplex(`List`, Some(`Long`)) == Right(InputType.List(InputType.Long))) &&
        assertTrue(decodeComplex(`Map`, Some(`Double`)) == Right(InputType.Map(InputType.Double))) &&
        assertTrue(decodeComplex(`List`, Some(`String`)) == Right(InputType.List(InputType.String))) &&
        assertTrue(decodeComplex(`Map`, Some(`Timestamp`)) == Right(InputType.Map(InputType.Timestamp))) &&
        assertTrue(decodeComplex(`List`, Some("Invalid")).isLeft) &&
        assertTrue(decodeComplex(`Map`, itemType = Some("Named")) == Right(InputType.Map(InputType.Named("Named")))) &&
        assertTrue(decodeComplex(`List`).isLeft) &&
        assertTrue(decodeComplex("Named") == Right(InputType.Named("Named")))
    },

    test("Requires type information.") {
      assertTrue(JsonObject().asJson.as[InputType].isLeft)
    }

  )

  /** Decodes a primitive input type. */
  private def decodePrimitive(primitiveType: String) =
    JsonObject(`PrimitiveType` -> primitiveType.asJson).asJson.as[InputType]

  /** Decodes a complex input type. */
  private def decodeComplex(_type: String, primitiveItemType: Option[String] = None, itemType: Option[String] = None) =
    JsonObject(
      Seq(`Type` -> _type.asJson) ++
        primitiveItemType.map(`PrimitiveItemType` -> _.asJson) ++
        itemType.map(`ItemType` -> _.asJson): _*
    ).asJson.as[InputType]

