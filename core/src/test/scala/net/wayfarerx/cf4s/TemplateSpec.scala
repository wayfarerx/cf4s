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

  "Template" should "render the AWS template format version" in {
    Template().asJson shouldBe Json.obj("AWSTemplateFormatVersion" -> "2010-09-09".asJson)
    new Template.Builder {}.build.asJson shouldBe Json.obj("AWSTemplateFormatVersion" -> "2010-09-09".asJson)
  }
