/* TemplateSpec.scala
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

import io.circe.Json
import io.circe.syntax.*

import org.scalatest.flatspec.*
import org.scalatest.matchers.should.*

/**
 * Test suite for templates.
 */
class TemplateSpec extends AnyFlatSpec with Matchers:

  private val AWSTemplateFormatVersion = "AWSTemplateFormatVersion" -> "2010-09-09".asJson

  "Template" should "build empty templates" in {
    new Template.Builder{}.build shouldBe Template()
  }

  it should "render the components of templates" in {
    new Template.Builder {

      override def description: String = "D"

      override def parameters: Seq[Parameter[_]] = super.parameters :+ Parameter[Int]("P")

      override def resources: Seq[Resource] = super.resources :+ new Resource:

        override def logicalName: String = "R"

        override def resourceType: String = "RT"

        override def resourceProperties: Option[Json] = None

    }.build.asJson shouldBe Json.obj(
      AWSTemplateFormatVersion,
      "Description" -> "D".asJson,
      "Parameters" -> Json.obj("P" -> Json.obj("Type" -> "Number".asJson)),
      "Resources" -> Json.obj("R" -> Json.obj(
        "Type" -> "RT".asJson
      ))
    )
  }
