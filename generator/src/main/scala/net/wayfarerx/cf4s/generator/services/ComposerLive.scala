/* ComposerLive.scala
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

import net.wayfarerx.cf4s.generator.model.Resource
import net.wayfarerx.cf4s.generator.code.CodeClass
import zio.{Task, ULayer, ZLayer}

/**
 * Implementation of the service that composes classes from model objects.
 */
case class ComposerLive() extends Composer:

  /* Compose classes derived from the specified resources. */
  override def compose(resources: Seq[Resource]): Task[Seq[CodeClass]] =
    ???

/**
 * Factory for composer service implementations.
 */
object ComposerLive:

  /** The layer that provides a composer service implementation. */
  val layer: ULayer[Composer] = ZLayer succeed ComposerLive()
