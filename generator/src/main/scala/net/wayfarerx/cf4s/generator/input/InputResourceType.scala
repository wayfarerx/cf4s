/* InputResourceType.scala
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

import io.circe.{Decoder, HCursor}

/**
 * Definition of an input resource type.
 *
 * @param id            The ID of this resource type.
 * @param documentation The link to the documentation for this resource type.
 * @param attributes    The attributes of this resource type.
 * @param properties    The properties of this resource type.
 */
case class InputResourceType(
  id: String,
  documentation: String,
  attributes: Seq[InputAttribute],
  properties: Seq[InputProperty]
)

/**
 * Factory for input resource type definitions.
 */
object InputResourceType:

  /** Decodes input resource types from a JSON cursor. */
  private[input] val decodeAll = decodeEntries { (id, cursor) =>
    for
      documentation <- cursor.downField(`Documentation`).as[String]
      attribute <- cursor.downField(`Attribute`).success.fold(Right(Seq.empty))(InputAttribute.decodeAll)
      attributes <- cursor.downField(`Attributes`).success.fold(Right(Seq.empty))(InputAttribute.decodeAll)
      property <- cursor.downField(`Property`).success.fold(Right(Seq.empty))(InputProperty.decodeAll)
      properties <- cursor.downField(`Properties`).success.fold(Right(Seq.empty))(InputProperty.decodeAll)
    yield InputResourceType(id, documentation, attribute ++ attributes, property ++ properties)
  }
