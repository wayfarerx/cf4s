/* InputSpecification.scala
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
 * Definition of an input specification.
 *
 * @param propertyTypes The property types in this specification.
 * @param resourceTypes The resource types in this specification.
 */
case class InputSpecification(propertyTypes: Seq[InputPropertyType], resourceTypes: Seq[InputResourceType])

/**
 * Factory for input specifications.
 */
object InputSpecification:

  /** The given JSON decoder for input specifications. */
  given Decoder[InputSpecification] = Decoder instance { cursor =>
    for
      property <- cursor.downField(`PropertyType`).success.fold(Right(Seq.empty))(InputPropertyType.decodeAll)
      properties <- cursor.downField(`PropertyTypes`).success.fold(Right(Seq.empty))(InputPropertyType.decodeAll)
      resource <- cursor.downField(`ResourceType`).success.fold(Right(Seq.empty))(InputResourceType.decodeAll)
      resources <- cursor.downField(`ResourceTypes`).success.fold(Right(Seq.empty))(InputResourceType.decodeAll)
    yield InputSpecification(property ++ properties, resource ++ resources)
  }
