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
    for
      _type <- decodeType(cursor)
      orPrimitive <- _type.fold(decodePrimitiveType(cursor))(Right apply Some(_))
      result <- orPrimitive.fold[Decoder.Result[InputType]](Left(
        DecodingFailure(DecodingFailure.Reason.CustomReason("Input type information not found."), cursor)
      ))(Right(_))
    yield result
  }

  private[this] def decodeType(cursor: HCursor): Decoder.Result[Option[InputType]] =
    cursor.downField(`Type`).success.fold(Right(None)) { _type =>
      for
        name <- _type.as[String]
        result <- name match
          case container @ (input.`List` | input.`Map`) =>
            for
              itemType <- decodeItemType(cursor)
              orPrimitiveItemType <- itemType.fold(decodePrimitiveItemType(cursor))(Right apply Some(_))
              item <- orPrimitiveItemType.fold[Decoder.Result[InputType]](Left(
                DecodingFailure(DecodingFailure.Reason.CustomReason("Input item type information not found."), cursor)
              ))(Right(_))
            yield container match
              case input.`List` => Some(List(item))
              case input.`Map` => Some(Map(item))
          case defined =>
            Right(Some(Defined(defined)))
      yield result
    }

  private[this] def decodePrimitiveType(cursor: HCursor): Decoder.Result[Option[InputType]] =
    cursor.downField(`PrimitiveType`).success.fold(Right(None)) { primitiveType =>
      for name <- primitiveType.as[String] yield name match
        case input.`Boolean` => Some(Boolean)
        case input.`Integer` => Some(Integer)
        case input.`Long` => Some(Long)
        case input.`Double` => Some(Double)
        case input.`String` => Some(String)
        case input.`Timestamp` => Some(Timestamp)
        case input.`Json` => Some(Json)
        case _ => None
    }

  private[this] def decodeItemType(cursor: HCursor): Decoder.Result[Option[InputType]] =
    cursor.downField(`ItemType`).success.fold(Right(None)) { itemType =>
      for name <- itemType.as[String] yield Some(Defined(name))
    }

  private[this] def decodePrimitiveItemType(cursor: HCursor): Decoder.Result[Option[InputType]] =
    cursor.downField(`PrimitiveItemType`).success.fold(Right(None)) { primitiveItemType =>
      for name <- primitiveItemType.as[String] yield name match
        case input.`Boolean` => Some(Boolean)
        case input.`Integer` => Some(Integer)
        case input.`Long` => Some(Long)
        case input.`Double` => Some(Double)
        case input.`String` => Some(String)
        case input.`Timestamp` => Some(Timestamp)
        case _ => None
    }

  /**
   * The "Boolean" primitive input type.
   */
  case object Boolean extends InputType

  /**
   * The "Integer" primitive input type.
   */
  case object Integer extends InputType

  /**
   * The "Long" primitive input type.
   */
  case object Long extends InputType

  /**
   * The "Double" primitive input type.
   */
  case object Double extends InputType

  /**
   * The "Timestamp" primitive input type.
   */
  case object Timestamp extends InputType

  /**
   * The "String" primitive input type.
   */
  case object String extends InputType

  /**
   * The "Json" primitive input type.
   */
  case object Json extends InputType

  /**
   * The "List" non-primitive input type.
   *
   * @param item The type of items in this list.
   */
  case class List(item: InputType) extends InputType

  /**
   * The "Map" non-primitive input type.
   *
   * @param item The type of values in this map.
   */
  case class Map(item: InputType) extends InputType

  /**
   * A reference to a defined non-primitive input type.
   *
   * @param name The name of the type.
   */
  case class Defined(name: String) extends InputType
