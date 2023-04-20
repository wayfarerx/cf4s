/* LinkerLive.scala
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

import zio.{Task, ULayer, ZIO, ZLayer}
import zio.interop.catz.*

import input.*
import model.*

/**
 * Implementation of the service that links input specifications into sequences of resources.
 */
case class LinkerLive() extends Linker:

  import LinkerLive.*

  /* Links the specified input specification into a sequence of resources. */
  override def link(specification: InputSpecification): Task[Seq[Resource]] = for
    data <- Data fromInput specification.propertyTypes
    resources <- State.foldLeft(specification.resourceTypes)(Map.empty[Id, Resource]) { (results, resourceType) =>
      for
        resource <- LinkerLive linkResourceType resourceType
        result <- if results contains resource.id then
          Linking fail new IllegalArgumentException(s"Duplicate resource definition: ${resource.id}.")
        else Linking succeed resource.id -> resource
      yield results + result
    } runA data
  yield resources.values.toSeq

/**
 * Factory for linker service implementations.
 */
object LinkerLive:

  /** The layer that provides a linker service implementation. */
  val layer: ULayer[Linker] = ZLayer succeed LinkerLive()

  /**
   * Links the specified input resource type.
   *
   * @param resourceType The input resource type to link.
   * @return The resource linked for the specified input resource type.
   */
  private def linkResourceType(resourceType: InputResourceType): Linking[Resource] = for
    id <- State lift Id.fromString(resourceType.id)
    _ <- Linking push id
    attributes <- State.foldLeft(resourceType.attributes)(Map.empty[Token, Type.Attribute]) { (attrs, attr) =>
      for
        name <- State lift Token.fromString(attr.name)
        _ <- if attrs contains name then
          Linking fail new IllegalArgumentException(s"Duplicate attribute definition: $name.")
        else Linking succeed ()
        attributeType <- linkAttributeType(attr._type)
      yield attrs + (name -> attributeType)
    }
    properties <- linkProperties(resourceType.properties)
    _ <- Linking.pop
  yield Resource(id, resourceType.documentation, attributes, properties)

  /**
   * Links the input property type found when searching for the specified name.
   *
   * @param name The name of the input property type to link.
   * @return The type definition linked for the specified input property type.
   */
  private[this] def linkPropertyType(name: Name): Linking[Type.Definition] = for
    resolved <- Linking resolve name
    result <- resolved.fold {
      for
        input <- Linking lookup name
        inputName <- State lift Name.fromString(input.name)
        _ <- Linking push inputName
        properties <- linkProperties(input.properties)
        _ <- Linking.pop
        definition <- Linking define Type.Definition(inputName, input.documentation, properties)
      yield definition
    }(Linking.succeed)
  yield result

  /**
   * Links a collection of input properties to their associated model properties.
   *
   * @param input The collection of input properties to link.
   * @return The collection of input properties linked to their associated model properties.
   */
  private[this] def linkProperties(input: Iterable[InputProperty]): Linking[Map[Token, Property]] =
    State.foldLeft(input)(Map.empty) { (properties, inputProperty) =>
      for
        propertyName <- State lift Token.fromString(inputProperty.name)
        _ <- if properties contains propertyName then
          Linking fail new IllegalArgumentException(s"Duplicate property definition: $propertyName.")
        else Linking succeed ()
        propertyType <- linkType(inputProperty._type)
      yield properties + (propertyName -> Property(
        propertyType,
        inputProperty.required,
        inputProperty.documentation
      ))
    }

  /**
   * Links an input type to the associated model type.
   *
   * @param inputType The input type to link.
   * @return The linked associated model type.
   */
  private[this] def linkType(inputType: InputType): Linking[Type] = inputType match
    case InputType.Map(item) => linkItemType(item) map Type.Map.apply
    case attributeType => for result <- linkAttributeType(attributeType) yield result

  /**
   * Links an input attribute type to the associated model attribute type.
   *
   * @param inputType The input attribute type to link.
   * @return The linked associated model attribute type.
   */
  private[this] def linkAttributeType(inputType: InputType): Linking[Type.Attribute] = inputType match
    case InputType.Json => Linking succeed Type.Json
    case InputType.List(item) => linkItemType(item) map Type.List.apply
    case itemType => for result <- linkItemType(itemType) yield result

  /**
   * Links an input item type to the associated model item type.
   *
   * @param inputItemType The input item type to link.
   * @return The linked associated model item type.
   */
  private[this] def linkItemType(inputItemType: InputType): Linking[Type.Item] = inputItemType match
    case InputType.Boolean => Linking succeed Type.Boolean
    case InputType.Integer => Linking succeed Type.Integer
    case InputType.Long => Linking succeed Type.Long
    case InputType.Double => Linking succeed Type.Double
    case InputType.Timestamp => Linking succeed Type.Timestamp
    case InputType.String => Linking succeed Type.String
    case InputType.Defined(typeName) => for {
      name <- State lift Name.fromString(typeName)
      result <- linkPropertyType(name)
    } yield result
    case invalid =>
      for result <- Linking fail new IllegalArgumentException(s"Invalid item type: $invalid.") yield result

  /**
   * The data used by the linker while it is linking.
   *
   * @param propertyTypes The property types provided to the linker.
   * @param stack         The stack of names that the linker is linking.
   * @param definitions   The types defined by the linker.
   */
  private case class Data (
    propertyTypes: Map[Name, InputPropertyType] = Map.empty,
    stack: List[Either[Id, Name]] = Nil,
    definitions: Map[Name, Type.Definition] = Map.empty
  )

  /**
   * Factory for the data used by the linker while it is linking.
   */
  private object Data:

    /**
     * Creates data from a collection of input property types.
     *
     * @param inputPropertyTypes The input property types to index.
     * @return Data from a collection of input property types.
     */
    def fromInput(inputPropertyTypes: Iterable[InputPropertyType]): Task[Data] =
      ZIO.foldLeft(inputPropertyTypes)(Map.empty[Name, InputPropertyType]) { (results, inputPropertyType) =>
        for
          name <- Name fromString inputPropertyType.name
          result <- if results contains name then
            ZIO fail new IllegalArgumentException(s"Duplicate property type definition: $name.")
          else ZIO succeed name -> inputPropertyType
        yield results + result
      } map (Data(_))

  /** The type of state used by the linker service implementation. */
  private type Linking[T] = State[Data, T]

  /**
   * Factory for the type of state used by the linker service implementation.
   */
  private object Linking:

    /**
     * Returns the value as a success.
     *
     * @tparam A The type of the value to return.
     * @param value The value to return.
     * @return The value as a success.
     */
    def succeed[A](value: A): Linking[A] = State succeed value

    /**
     * Returns a failure.
     *
     * @param thrown The failure that was thrown.
     * @return A failure.
     */
    def fail(thrown: Throwable): Linking[Nothing] = State fail thrown

    /**
     * Attempts to look up an input property type.
     *
     * @param name The name of the input property type to look up.
     * @return The result of attempting to look up an input property type.
     */
    def lookup(name: Name): Linking[InputPropertyType] = for
      found <- search(name)(_.propertyTypes)
      result <- found.fold(fail(new IllegalArgumentException(s"Property type not found: $name.")))(succeed)
    yield result

    /**
     * Attempts to resolve a type that has been defined.
     *
     * @param name The name of the type to resolve.
     * @return The result of attempting to resolve a type that has been defined.
     */
    def resolve(name: Name): Linking[Option[Type.Definition]] =
      search(name)(_.definitions)

    /**
     * Searches an index for a specified name.
     *
     * @param name The name to search for.
     * @param f    The function that returns the index to search.
     * @return The result of searching an index for a specified name.
     */
    private def search[T](name: Name)(f: Data => Map[Name, T]): Linking[Option[T]] = State inspect { data =>
      val index = f(data)
      index get name orElse {
        if name.qualifier.isEmpty then
          data.stack collectFirst {
            case Left(id) => id
            case Right(Name(Some(id), _)) => id
          } flatMap (id => index get name.copy(qualifier = Some(id)))
        else None
      }
    }

    /**
     * Pushes an ID onto the stack if that ID is not already on the stack.
     *
     * @param id The ID to push onto the stack.
     * @return The result of pushing an ID onto the stack.
     */
    def push(id: Id): Linking[Unit] = push(Left(id))

    /**
     * Pushes a name onto the stack if that name is not already on the stack.
     *
     * @param name The name to push onto the stack.
     * @return The result of pushing a name onto the stack.
     */
    def push(name: Name): Linking[Unit] = push(Right(name))

    /**
     * Pushes a frame onto the stack if that frame is not already on the stack.
     *
     * @param frame The frame to push onto the stack.
     * @return The result of pushing a frame onto the stack.
     */
    private def push(frame: Either[Id, Name]): Linking[Unit] = for
      data <- State.get
      _ <- if data.stack contains frame then
        val trace = (frame :: (frame :: data.stack.takeWhile(_ != frame)).reverse)
          .map(_.fold(_.toString, _.toString)).mkString(" -> ")
        fail(new IllegalArgumentException(s"Recursive type definition: $trace."))
      else State.modify[Data](_data => _data.copy(stack = frame :: _data.stack))
    yield ()

    /**
     * Pops the top frame off the stack if it is not empty.
     *
     * @return The result of popping the top frame off the stack.
     */
    def pop: Linking[Unit] = for
      _ <- State.modify[Data](_data => _data.copy(stack = _data.stack.tail))
    yield ()

    /**
     * Defines a new type.
     *
     * @param definition The type to define.
     * @return The newly defined type.
     */
    def define(definition: Type.Definition): Linking[Type.Definition] = for
      _ <- State.modify[Data](_data => _data.copy(definitions = _data.definitions + (definition.name -> definition)))
    yield definition
