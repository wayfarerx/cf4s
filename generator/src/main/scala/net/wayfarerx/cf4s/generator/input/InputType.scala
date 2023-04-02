/* InputType.scala
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
package input

import io.circe.{ACursor, Decoder, DecodingFailure, HCursor}

/**
 * Base type for input types.
 */
sealed trait InputType extends Product

/**
 * Definition of the supported input types.
 */
object InputType:

  /** The given JSON decoder for input types. */
  given Decoder[InputType] = Decoder instance { cursor =>
    cursor.downField(`PrimitiveType`).success match
      case Some(primitiveType) =>
        Primitive.decode(primitiveType)
      case None =>
        cursor.downField(`Type`).success match
          case Some(complexType) => Complex.decode(
            complexType,
            cursor.downField(`PrimitiveItemType`),
            cursor.downField(`ItemType`)
          )
          case None => Left(DecodingFailure(
            DecodingFailure.Reason.CustomReason("Input type information not found."),
            cursor
          ))
  }

  /**
   * Base type for item input types.
   */
  sealed trait Item extends InputType

  /**
   * Support for item input types.
   */
  private object Item:

    /**
     * Decodes an item input type from the from the specified cursors.
     *
     * @param cursor          The cursor pointing at the type container.
     * @param primitiveCursor The cursor pointing at the primitive item input type.
     * @param complexCursor   The cursor pointing at the non-primitive item input type.
     * @return The decoded item input type.
     */
    private[InputType] def decode(cursor: HCursor, primitiveCursor: ACursor, complexCursor: ACursor) =
      primitiveCursor.success match
        case Some(primitive) => primitive.as[String] flatMap {
          case input.`Boolean` => Right(Boolean)
          case input.`Integer` => Right(Integer)
          case input.`Long` => Right(Long)
          case input.`Double` => Right(Double)
          case input.`String` => Right(String)
          case input.`Timestamp` => Right(Timestamp)
          case invalid => Left(DecodingFailure(
            DecodingFailure.Reason.CustomReason(s"Invalid primitive item input type: $invalid."),
            primitiveCursor
          ))
        }
        case None => complexCursor.success match
          case Some(complex) => complex.as[String] map Named.apply
          case None => Left(DecodingFailure(
            DecodingFailure.Reason.CustomReason("Item input type information not found."),
            cursor
          ))

  /**
   * Base type for primitive input types.
   */
  sealed trait Primitive extends InputType

  /**
   * Definition of the supported primitive input types.
   */
  private object Primitive:

    /**
     * Decodes a primitive input type from the from the specified cursor.
     *
     * @param cursor The cursor pointing to an object containing type information.
     * @return The decoded primitive input type.
     */
    private[InputType] def decode(cursor: HCursor) =
      cursor.as[String] flatMap {
        case input.`Boolean` => Right(Boolean)
        case input.`Integer` => Right(Integer)
        case input.`Long` => Right(Long)
        case input.`Double` => Right(Double)
        case input.`String` => Right(String)
        case input.`Timestamp` => Right(Timestamp)
        case input.`Json` => Right(Json)
        case invalid => Left(DecodingFailure(
          DecodingFailure.Reason.CustomReason(s"Invalid primitive input type: $invalid."),
          cursor
        ))
      }

  /**
   * Base type for non-primitive input types.
   */
  sealed trait Complex extends InputType

  /**
   * Definition of the supported non-primitive input types.
   */
  private object Complex:

    /**
     * Decodes a non-primitive input type from the from the specified cursor.
     *
     * @param cursor          The cursor pointing to an object containing type information.
     * @param primitiveCursor A cursor pointing to the primitive item input type.
     * @param complexCursor   A cursor pointing to the non-primitive item input type.
     * @return The decoded non-primitive input type.
     */
    private[InputType] def decode(cursor: HCursor, primitiveCursor: ACursor, complexCursor: ACursor) =
      cursor.as[String] flatMap {
        case input.`List` => Item.decode(cursor, primitiveCursor, complexCursor) map List.apply
        case input.`Map` => Item.decode(cursor, primitiveCursor, complexCursor) map Map.apply
        case typeName => Right(Named(typeName))
      }

  /**
   * The "Boolean" primitive input type.
   */
  case object Boolean extends Primitive with Item

  /**
   * The "Integer" primitive input type.
   */
  case object Integer extends Primitive with Item

  /**
   * The "Long" primitive input type.
   */
  case object Long extends Primitive with Item

  /**
   * The "Double" primitive input type.
   */
  case object Double extends Primitive with Item

  /**
   * The "Timestamp" primitive input type.
   */
  case object Timestamp extends Primitive with Item

  /**
   * The "String" primitive input type.
   */
  case object String extends Primitive with Item

  /**
   * The "Json" primitive input type.
   */
  case object Json extends Primitive

  /**
   * The "List" non-primitive input type.
   *
   * @param item The type of items in this list.
   */
  case class List(item: Item) extends Complex

  /**
   * The "Map" non-primitive input type.
   *
   * @param item The type of values in this map.
   */
  case class Map(item: Item) extends Complex

  /**
   * A reference to a named non-primitive input type.
   *
   * @param name The name of the type.
   */
  case class Named(name: String) extends Complex with Item
