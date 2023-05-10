/* ResourceSpec.scala
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
 * Test suite for resources.
 */
class ResourceSpec extends AnyFlatSpec with Matchers:

  "Resource" should "represent resources" in {
    MockResource("MyResource", "WX::Resource", Some(Json.obj("Active" -> true.asJson))).render shouldBe
      Some(Json.obj("Type" -> "WX::Resource".asJson, "Properties" -> Json.obj("Active" -> true.asJson)))
  }

  it should "convert into a data reference" in {
    val myData: Data[MockResource] = MockResource("MyResource", "WX::Resource", None)
    myData.asJson shouldBe Fn.ref("MyResource")
  }

  case class MockResource(logicalName: String, resourceType: String, resourceProperties: Option[Json]) extends Resource
