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

import cats.data.{NonEmptySeq, StateT}

import zio.{Task, ULayer, ZIO, ZLayer}
import zio.interop.catz.*

import model.*
import output.*

/**
 * Implementation of the service that composes classes from model objects.
 */
case class ComposerLive() extends Composer:

  import ComposerLive.*

  /* Compose classes derived from the specified resources. */
  override def compose(resources: Seq[Resource]): Task[Seq[OutputClass]] =
    composing(resources.toList) flatMap (_ => output) runA Composition()

/**
 * Factory for composer service implementations.
 */
object ComposerLive:

  /** The layer that provides a composer service implementation. */
  val layer: ULayer[Composer] = ZLayer succeed ComposerLive()

  /** The type of composition state transitions. */
  private type State[T] = StateT[Task, Composition, T]

  /** Access to the current composition within a transition. */
  private object State:

    /** Returns the current composition. */
    def composition: State[Composition] = StateT.get

    /**
     * Returns a value from the current state.
     *
     * @tparam T The type of value to return.
     * @param f The function that returns the specified value.
     * @return The returned value as a state.
     */
    def apply[T](f: Composition => Task[T]): State[T] =
      StateT(composition => f(composition) map (composition -> _))

  private def composing(resources: List[Resource]): State[Unit] = resources match
    case head :: tail =>
      ???
    case Nil =>
      State(_ => ZIO.unit)

  private[this] def composeResource(resource: Resource): State[OutputClass.Resource] =
    ???

  private[this] def composeProperty(property: Property): State[OutputClass.Property] =
    ???

  private def output: State[Seq[OutputClass]] = State.composition flatMap { composition =>
    val properties = composition.properties.groupMap((name, _) => name.qualifier)((_, p) => p)
    val companions = properties collect {
      case (Some(id), _properties) if _properties.nonEmpty =>
        id -> OutputObject(NonEmptySeq fromSeqUnsafe _properties.toSeq)
    }

//    ZIO.foldLeft((composition.resources.keys ++ companions.keys).toSet)(composition.resources) { (resources, id) =>
//      resources get id match
//        case Some(resource) =>
//          ZIO succeed resources + (id -> resource.copy(companion = companions get id))
//        case None =>
//          ZIO fail new IllegalStateException(s"""Cannot define properties for unknown resource "$id".""")
//    } map (_.values ++ properties.getOrElse(None, Iterable.empty))

    render((composition.resources.keys ++ companions.keys).toSet.toList, companions)
  }

  private[this] def render(keys: List[Id], companions: Map[Id, OutputObject]): State[Seq[OutputClass]] = keys match
    case head :: tail => ???
    case Nil => State(_ => ZIO succeed Seq.empty)

  /**
   * The state of the composition process.
   *
   * @param resources  The index of resources that have been composed.
   * @param properties The index of properties that have been composed.
   */
  private case class Composition(
    resources: Map[Id, OutputClass.Resource] = Map.empty,
    properties: Map[Name, OutputClass.Property] = Map.empty
  ) {

    /**
     * Composes classes for the the specified resource, adding them to this composition.
     *
     * @param resource The resource to compose.
     * @return The next state of this composition.
     */
    def composeResource2(resource: Resource): Task[Composition] = for {
      _ <- resources.get(resource.id).fold(ZIO.unit) { _ =>
        ZIO fail new IllegalArgumentException(s"""Cannot compose resource "${resource.id}" more than once.""")
      }
      result <- {
        val start = OutputClass.Resource(
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

    private def composeProperty2(property: Property): Task[Composition] =
      property._type match
        case definition@Type.Definition(_, _, _) => composeDefinition2(definition)
        case Type.List(definition@Type.Definition(_, _, _)) => composeDefinition2(definition)
        case Type.Map(definition@Type.Definition(_, _, _)) => composeDefinition2(definition)
        case _ => ZIO succeed this

    private def composeDefinition2(definition: Type.Definition): Task[Composition] =
      if properties contains definition.name then ZIO succeed this else {
        definition.properties
        val result = OutputClass.Property(definition.name.toString, ???, definition.documentation)
        ZIO succeed copy(properties = properties + (definition.name -> result))
      }

    /** Returns the classes generated by this composition. */
    def classes2: Task[Iterable[OutputClass]] =
      val propertiesByResourceId = properties.groupMap((name, _) => name.qualifier)((_, property) => property)
      val companions = propertiesByResourceId collect {
        case (Some(id), _properties) if _properties.nonEmpty =>
          id -> OutputObject(NonEmptySeq fromSeqUnsafe _properties.toSeq)
      }
      ZIO.foldLeft((resources.keys ++ companions.keys).toSet)(resources) { (_resources, id) =>
        _resources get id match
          case Some(resource) =>
            ZIO succeed _resources + (id -> resource.copy(companion = companions get id))
          case None =>
            ZIO fail new IllegalStateException(s"""Cannot define properties for unknown resource "$id".""")
      } map (_.values ++ propertiesByResourceId.getOrElse(None, Iterable.empty))

  }
