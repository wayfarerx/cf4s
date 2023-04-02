/* InputAttribute.scala
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
 * Definition of an input attribute.
 *
 * @param name  The name of this attribute.
 * @param _type The type of this attribute.
 */
case class InputAttribute(name: String, _type: InputType)

/**
 * Factory for input attributes.
 */
object InputAttribute:

  /** Decodes input attributes from a JSON cursor. */
  private[input] val decodeAll = decodeEntries { (name, cursor) =>
    for
      _type <- cursor.as[InputType]
    yield InputAttribute(name, _type)
  }
