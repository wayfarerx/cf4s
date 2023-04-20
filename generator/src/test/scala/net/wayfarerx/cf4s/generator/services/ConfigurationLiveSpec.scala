/* ConfigurationLiveSpec.scala
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

import java.net.URL

import zio.{Task, ULayer, ZIO, ZLayer}
import zio.test.*

/**
 * Test suite for live configuration services.
 */
object ConfigurationLiveSpec extends ZIOSpecDefault:

  /** The specifications URL to use when testing. */
  private lazy val specificationsUrl = {
    for
      url <- ZIO attempt getClass.getClassLoader.getResource("net/wayfarerx/cf4s/generator/main/us-east-2.zip")
      result <- Option(url).fold(ZIO fail new IllegalStateException(
        "CloudFormation specifications not found."
      ))(ZIO.succeed)
    yield result
  }.orDie.memoize.flatten

  /** The configuration layer to use when testing. */
  lazy val layer: ULayer[Configuration] = for
    _specificationsUrl <- ZLayer fromZIO specificationsUrl
    result <- ConfigurationLive layer _specificationsUrl.get
  yield result

  /** The tests that validate configurations. */
  override def spec: Spec[Any, Nothing] = suite(classOf[ConfigurationLive].getName)(

    test("Provides the configuration data.") {
      for
        _specificationsUrl <- specificationsUrl
        result <- assertZIO(Configuration.specificationsUrl)(Assertion equalTo _specificationsUrl)
      yield result
    } provide layer

  )
