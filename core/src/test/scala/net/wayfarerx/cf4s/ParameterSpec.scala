/* ParameterSpec.scala
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

package net.wayfarerx.cf4s

import scala.language.implicitConversions

import io.circe.Json
import io.circe.syntax.*

import org.scalatest.flatspec.*
import org.scalatest.matchers.should.*

/**
 * Test suite for parameters.
 */
class ParameterSpec extends AnyFlatSpec with Matchers:

  "Parameter" should "represent number parameters" in {
    Parameter[Int]("MyInt").render shouldBe
      Some(Json.obj("Type" -> "Number".asJson))
    Parameter[Float](
      "MyFloat",
      default = 7f,
      allowedValues = Seq(6f, 7f, 8f),
      minValue = 6f,
      maxValue = 8f
    ).render shouldBe Some(Json.obj(
      "Type" -> "Number".asJson,
      "Default" -> 7f.asJson,
      "AllowedValues" -> Seq(6f, 7f, 8f).asJson,
      "MinValue" -> 6f.asJson,
      "MaxValue" -> 8f.asJson
    ))
  }

  it should "represent number sequence parameters" in {
    Parameter[Seq[Long]]("MyLongs").render shouldBe
      Some(Json.obj("Type" -> "List<Number>".asJson))
  }

  it should "represent string parameters" in {
    Parameter[String]("MyString").render shouldBe
      Some(Json.obj("Type" -> "String".asJson))
    Parameter[String](
      "MyString",
      default = "A",
      allowedPattern = "[A-C]+",
      minLength = 1,
      maxLength = 5,
      noEcho = true,
      constraintDescription = "foo",
      description = "FOO"
    ).render shouldBe Some(Json.obj(
      "Type" -> "String".asJson,
      "Default" -> "A".asJson,
      "AllowedPattern" -> "[A-C]+".asJson,
      "MinLength" -> 1.asJson,
      "MaxLength" -> 5.asJson,
      "NoEcho" -> true.asJson,
      "ConstraintDescription" -> "foo".asJson,
      "Description" -> "FOO".asJson
    ))
  }

  it should "represent string sequence parameters" in {
    Parameter[Seq[String]]("MyStrings").render shouldBe
      Some(Json.obj("Type" -> "CommaDelimitedList".asJson))
  }

  it should "render named entries" in {
    Entry.named(Parameter[String]("MyString")) shouldBe Some(
      "MyString" -> Json.obj("Type" -> "String".asJson)
    )
  }

  it should "convert into a data reference" in {
    val myData: Data[String] = Parameter[String]("MyString")
    myData.asJson shouldBe Fn.ref("MyString")
  }
