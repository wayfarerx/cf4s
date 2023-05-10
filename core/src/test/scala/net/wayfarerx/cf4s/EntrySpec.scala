/* EntrySpec.scala
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
 * Test suite for entries.
 */
class EntrySpec extends AnyFlatSpec with Matchers:

  "Entry" should "create entries" in {
    Entry("Value", 7) shouldBe Some("Value" -> 7.asJson)
    Entry.option[Int]("Option", None) shouldBe None
    Entry.option("Option", 7) shouldBe Some("Option" -> 7.asJson)
    Entry.string("String", "") shouldBe None
    Entry.string("String", "foo") shouldBe Some("String" -> "foo".asJson)
    Entry.seq("Seq", Seq.empty[Int]) shouldBe None
    Entry.seq("Seq", Seq(7, 8)) shouldBe Some("Seq" -> Seq(7, 8).asJson)
    Entry.collection("Collection", Seq.empty *) shouldBe None
    Entry.collection("Collection", Entry("A", 7), None) shouldBe
      Some("Collection" -> Json.obj("A" -> 7.asJson))
    Entry.named(MockNamed("Named", None)) shouldBe None
    Entry.named(MockNamed("Named", 7.asJson)) shouldBe Some("Named" -> 7.asJson)
  }

  case class MockNamed(logicalName: String, render: Option[Json]) extends Named
