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

import zio.{Task, ULayer, ZIO, ZLayer}

import model._
import output._

/**
 * Implementation of the service that composes classes from model objects.
 */
case class ComposerLive() extends Composer:

  /* Compose classes derived from the specified resources. */
  override def compose(resources: Seq[Resource]): Task[Seq[CodeClass]] =
    ZIO.foldLeft(resources)(ComposerLive.Composition())(ComposerLive.compose) map { composition =>
      composition.properties.values ++ composition.resources.values
    } map (_.toSeq)

/**
 * Factory for composer service implementations.
 */
object ComposerLive:

  /** The state of the composer while it is composing. */
  private type Composing[T] = StateT[Task, Composition, T]

  /** The layer that provides a composer service implementation. */
  val layer: ULayer[Composer] = ZLayer succeed ComposerLive()

  /**
   * The state of the composition process.
   *
   * @param properties The index of top-level properties that have been composed.
   * @param resources  The index of resources that have been composed.
   */
  private case class Composition(
    properties: Map[Token, CodeClass.Property] = Map.empty,
    resources: Map[Id, CodeClass.Resource] = Map.empty
  )

  /**
   * Composes classes for the the specified resource, adding them to the supplied composition.
   *
   * @param composition The previous state of the composition.
   * @param resource    The resource to compose.
   * @return The next state of the composition.
   */
  private def compose(composition: Composition, resource: Resource): Task[Composition] = for {
    _ <- composition.resources.get(resource.id).fold(ZIO.unit) { _ =>
      ZIO.fail(new IllegalArgumentException(s"""Cannot compose resource "${resource.id}" more than once."""))
    }
    result <- {
      val start = CodeClass.Resource(
        resource.id.tokens.last.toString,
        resource.id.toString,
        documentation = resource.documentation
      )
//      for {
//        fields <- ZIO.foldLeft(resource.)
//      }
      ZIO.unit // FIXME
    }

  } yield ??? // FIXME

  private def composeField(field: Any): Composing[CodeField] = ???
