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

import cats.data.StateT

import zio.{Task, ULayer, ZLayer}

import model._
import code._

/**
 * Implementation of the service that composes classes from model objects.
 */
case class ComposerLive() extends Composer:

  import ComposerLive.Composed

  /* Compose classes derived from the specified resources. */
  override def compose(resources: Seq[Resource]): Task[Seq[CodeClass]] =
    val composed = Composed(resources.foldLeft(Map.empty)((rs, r) => rs + (r.id -> r)))
    val v = composed.resolve(resources.head.id)
    //v.run
    ???

/**
 * Factory for composer service implementations.
 */
object ComposerLive:

  /** The layer that provides a composer service implementation. */
  val layer: ULayer[Composer] = ZLayer succeed ComposerLive()

  private type Composition = StateT[Task, Composed, CodeClass]

  private case class Composed(resources: Map[Id, Resource], classes: Seq[CodeClass] = Seq.empty):

    def resolve(resource: Id): Composition = {

      ???
    }
