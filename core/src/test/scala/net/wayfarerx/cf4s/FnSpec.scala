/* FnSpec.scala
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
 * Test suite for functions.
 */
class FnSpec extends AnyFlatSpec with Matchers:

  "Fn::GetAtt" should "construct get attribute function invocations" in {
    Fn.getAtt[String]("Name", "Att") shouldBe
      Json.obj("Fn::GetAtt" -> Json.arr("Name".asJson, "Att".asJson))
  }

  "Fn::Join" should "construct join function invocations" in {
    Fn.join(",", Seq("A", "B".asJson)) shouldBe
      Json.obj("Fn::Join" -> Json.arr(",".asJson, Json.arr("A".asJson, "B".asJson)))
  }

  "Fn::Select" should "construct select function invocations" in {
    Fn.select(1, Seq[Data[String]]("A", "B".asJson)) shouldBe
      Json.obj("Fn::Select" -> Json.arr(1.asJson, Json.arr("A".asJson, "B".asJson)))
  }

  "Fn::Split" should "construct split function invocations" in {
    Fn.split(",", "A,B,C".asJson) shouldBe
      Json.obj("Fn::Split" -> Json.arr(",".asJson, "A,B,C".asJson))
  }

  "Fn::Sub" should "construct substitution function invocations" in {
    Fn.sub("region: ${AWS::Region}") shouldBe
      Json.obj("Fn::Sub" -> "region: ${AWS::Region}".asJson)
    Fn.sub("target: $Target", "Target" -> "TARGET") shouldBe
      Json.obj("Fn::Sub" -> Json.arr("target: $Target".asJson, Json.obj("Target" -> "TARGET".asJson)))
  }

  "Ref" should "construct references" in {
    Fn.ref[Int]("Foo") shouldBe Json.obj("Ref" -> "Foo".asJson)
  }
