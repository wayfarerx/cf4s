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
   * Base type for primitive types.
   */
  sealed trait Primitive extends Type

  /**
   * Base type for non-primitive types.
   */
  sealed trait Complex extends Type

  /**
   * Base type for item types.
   */
  sealed trait Item extends Type

  /**
   * The "Boolean" primitive and item type.
   */
  case object Boolean extends Primitive with Item

  /**
   * The "Integer" primitive and item type.
   */
  case object Integer extends Primitive with Item

  /**
   * The "Long" primitive and item type.
   */
  case object Long extends Primitive with Item

  /**
   * The "Double" primitive and item type.
   */
  case object Double extends Primitive with Item

  /**
   * The "Timestamp" primitive and item type.
   */
  case object Timestamp extends Primitive with Item

  /**
   * The "String" primitive and item type.
   */
  case object String extends Primitive with Item

  /**
   * The "Json" primitive type.
   */
  case object Json extends Primitive

  /**
   * The "List" non-primitive type.
   *
   * @param item The type of items in this list.
   */
  case class List(item: Item) extends Complex

  /**
   * The "Map" non-primitive type.
   *
   * @param item The type of values in this map.
   */
  case class Map(item: Item) extends Complex

  /**
   * A type definition with properties.
   *
   * @param name          The name of this type definition.
   * @param documentation The link to the documentation for this type.
   * @param properties    The properties to define on this type.
   */
  case class Definition(name: Name, documentation: String, properties: Seq[Property]) extends Complex with Item

