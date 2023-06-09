/* InputPropertyType.scala
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
 * Definition of an input property type.
 *
 * @param name          The name of this property type.
 * @param documentation The link to the documentation for this property type.
 * @param properties    The sub-properties defined by this property type.
 */
case class InputPropertyType(name: String, documentation: String, properties: Seq[InputProperty])

/**
 * Factory for input property types.
 */
object InputPropertyType:

  /** Decodes input property types from a JSON cursor. */
  private[input] val decodeAll = decodeEntries { (name, cursor) =>
    for
      documentation <- cursor.downField(`Documentation`).as[String]
      property <- cursor.downField(`Property`).success.fold(Right(Seq.empty))(InputProperty.decodeAll)
      properties <- cursor.downField(`Properties`).success.fold(Right(Seq.empty))(InputProperty.decodeAll)
    yield InputPropertyType(name, documentation, property ++ properties)
  }
