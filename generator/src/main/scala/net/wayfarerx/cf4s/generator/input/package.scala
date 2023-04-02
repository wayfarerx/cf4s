/* package.scala
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

/** Alias for the result type. */
private[input] type Result[T] = Decoder.Result[T]

/** The "Attribute" constant. */
private[input] val `Attribute` = "Attribute"

/** The "Attributes" constant. */
private[input] val `Attributes` = "Attributes"

/** The "Boolean" constant. */
private[input] val `Boolean` = "Boolean"

/** The "Documentation" constant. */
private[input] val `Documentation` = "Documentation"

/** The "Double" constant. */
private[input] val `Double` = "Double"

/** The "Integer" constant. */
private[input] val `Integer` = "Integer"

/** The "ItemType" constant. */
private[input] val `ItemType` = "ItemType"

/** The "Json" constant. */
private[input] val `Json` = "Json"

/** The "List" constant. */
private[input] val `List` = "List"

/** The "Long" constant. */
private[input] val `Long` = "Long"

/** The "Map" constant. */
private[input] val `Map` = "Map"

/** The "PrimitiveItemType" constant. */
private[input] val `PrimitiveItemType` = "PrimitiveItemType"

/** The "PrimitiveType" constant. */
private[input] val `PrimitiveType` = "PrimitiveType"

/** The "Property" constant. */
private[input] val `Property` = "Property"

/** The "Properties" constant. */
private[input] val `Properties` = "Properties"

/** The "PropertyType" constant. */
private[input] val `PropertyType` = "PropertyType"

/** The "PropertyTypes" constant. */
private[input] val `PropertyTypes` = "PropertyTypes"

/** The "Required" constant. */
private[input] val `Required` = "Required"

/** The "ResourceType" constant. */
private[input] val `ResourceType` = "ResourceType"

/** The "ResourceTypes" constant. */
private[input] val `ResourceTypes` = "ResourceTypes"

/** The "String" constant. */
private[input] val `String` = "String"

/** The "Timestamp" constant. */
private[input] val `Timestamp` = "Timestamp"

/** The "Type" constant. */
private[input] val `Type` = "Type"

/**
 * Decodes the entries in the specified cursor, applying a function to each entry and collecting any results.
 *
 * @tparam T The type of result that is returned for each entry.
 * @param f The function that converts an entry into a result.
 * @return The results of decoding the entries in the specified cursor.
 */
private[input] def decodeEntries[T](f: (String, HCursor) => Result[T]): HCursor => Result[Seq[T]] = cursor =>
  cursor.keys.fold(Right(Seq.empty))(_.foldLeft[Result[Seq[T]]](Right(Seq.empty)) { (results, key) =>
    for
      _results <- results
      result <- cursor.downField(key).success.fold(Right(None))(f(key, _) map (Option(_)))
    yield _results ++ result
  })