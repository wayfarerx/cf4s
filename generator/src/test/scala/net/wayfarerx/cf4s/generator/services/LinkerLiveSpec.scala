/* LinkerLiveSpec.scala
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
package services

import zio.ZLayer
import zio.test.*

import input.*
import model.*

/**
 * Test suite for live linker services.
 */
object LinkerLiveSpec extends ZIOSpecDefault:

  /** The tests that validate linkers. */
  override def spec: Spec[Any, Throwable] = suite(classOf[LinkerLive].getName)(

    test("Links valid input specifications.") {
      val specification = InputSpecification(
        Seq(
          InputPropertyType("AWS::S3::Bucket.PT1", "pt1", Seq(
            InputProperty("P1", InputType.String, true, "p1")
          )),
          InputPropertyType("PT2", "pt2", Seq(
            InputProperty("P2", InputType.Defined("PT1"), true, "p2")
          ))
        ),
        Seq(
          InputResourceType("AWS::S3::Bucket", "bucket", Seq(
            InputAttribute("A1", InputType.Defined("PT1"))
          ), Seq(
            InputProperty("P3", InputType.Defined("PT2"), true, "p3")
          ))
        )
      )
      for
        resources <- Linker.link(specification)
        result <- assertTrue(resources.size == 1) && assertTrue(resources.head.id.toString == "AWS::S3::Bucket")
      yield result
    } provide LinkerLive.layer,

    test("Does not allow for duplicate resource type definitions.") {
      val specification = InputSpecification(Seq(), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq()),
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq())
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer,

    test("Does not allow for duplicate property type definitions.") {
      val specification = InputSpecification(Seq(
        InputPropertyType("AWS::S3::Bucket.PT", "pt", Seq(
          InputProperty("P1", InputType.String, true, "p1")
        )),
        InputPropertyType("AWS::S3::Bucket.PT", "pt", Seq(
          InputProperty("P2", InputType.Timestamp, false, "p2")
        ))
      ), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq(
          InputProperty("P3", InputType.Defined("PT"), true, "p3")
        ))
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer,

    test("Does not allow for duplicate attribute definitions.") {
      val specification = InputSpecification(Seq(), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(
          InputAttribute("A1", InputType.String),
          InputAttribute("A1", InputType.Timestamp)
        ), Seq())
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer,

    test("Does not allow for duplicate property definitions.") {
      val specification = InputSpecification(Seq(), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq(
          InputProperty("P1", InputType.String, true, "p1"),
          InputProperty("P1", InputType.Timestamp, false, "p1")
        ))
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer,

    test("Does not support invalid item types.") {
      val specification = InputSpecification(Seq(), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq(
          InputProperty("P1", InputType.List(InputType.Map(InputType.String)), true, "p1")
        ))
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer,

    test("Fails when property types cannot be resolved.") {
      val specification = InputSpecification(Seq(), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq(
          InputProperty("P1", InputType.Defined("AWS::S3::Bucket.Unknown"), true, "p1")
        ))
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer,

    test("Does not support recursive property types.") {
      val specification = InputSpecification(Seq(
        InputPropertyType("PT1", "pt1", Seq(
          InputProperty("P1", InputType.Defined("PT2"), true, "p1")
        )),
        InputPropertyType("PT2", "pt2", Seq(
          InputProperty("P2", InputType.Defined("PT1"), true, "p2")
        ))
      ), Seq(
        InputResourceType("AWS::S3::Bucket", "bucket", Seq(), Seq(
          InputProperty("P3", InputType.Defined("PT1"), true, "p1")
        ))
      ))
      for
        result <- Linker.link(specification).exit
      yield assert(result)(Assertion.failsWithA[IllegalArgumentException])
    } provide LinkerLive.layer

  )
