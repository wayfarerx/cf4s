/* RepositoryLiveSpec.scala
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

/**
 * Test suite for live repository services.
 */
object RepositoryLiveSpec extends ZIOSpecDefault:

  /** The tests that validate repositories. */
  override def spec: Spec[Any, Throwable] = suite(classOf[RepositoryLive].getName)(

    test("Loads specifications from the repository.") {
      for
        specifications <- Repository.load("S3BucketSpecification")
        result <- assertTrue(specifications.size == 1) && assertTrue(specifications.head._1 == "S3BucketSpecification")
      yield result
    } provide ZLayer.make[Repository](ConfigurationLiveSpec.layer, RepositoryLive.layer)

  )
