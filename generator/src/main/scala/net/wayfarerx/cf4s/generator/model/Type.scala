/* Type.scala
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
package model

/**
 * Base type for model types.
 */
sealed trait Type

/**
 * Definition of the supported model types.
 */
object Type:

  /**
   * Base type for attribute types.
   */
  sealed trait Attribute extends Type

  /**
   * Base type for item types.
   */
  sealed trait Item extends Attribute

  /**
   * The "Boolean" type.
   */
  case object Boolean extends Item

  /**
   * The "Integer" type.
   */
  case object Integer extends Item

  /**
   * The "Long" type.
   */
  case object Long extends Item

  /**
   * The "Double" type.
   */
  case object Double extends Item

  /**
   * The "String" type.
   */
  case object String extends Item

  /**
   * The "Timestamp" type.
   */
  case object Timestamp extends Item

  /**
   * The "Json" type.
   */
  case object Json extends Attribute

  /**
   * The "List" type.
   *
   * @param item The type of items in this list.
   */
  case class List(item: Item) extends Attribute

  /**
   * The "Map" type.
   *
   * @param item The type of values in this map.
   */
  case class Map(item: Item) extends Type

  /**
   * A type definition with properties.
   *
   * @param name          The name of this type.
   * @param documentation The link to the documentation for this type.
   * @param properties    The properties to define on this type.
   */
  case class Definition(name: Name, documentation: String, properties: collection.immutable.Map[Token, Property])
    extends Item

