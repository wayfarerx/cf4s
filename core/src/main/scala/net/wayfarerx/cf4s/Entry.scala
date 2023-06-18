/* Entry.scala
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

package net.wayfarerx.cf4s

import io.circe.{Encoder, Json}
import io.circe.syntax.*

/** An entry in a JSON object. */
type Entry = (String, Json)

/**
 * Factory for entries in JSON objects.
 */
object Entry:

  /**
   * Attempts to create an entry from a name and a value.
   *
   * @tparam A The type of value to encode.
   * @param name  The name of the entry to return.
   * @param value The value to encode in the entry.
   * @return The result of attempting to create an entry from a name and a value.
   */
  def apply[A: Encoder](name: String, value: A): Option[Entry] =
    Option(value) map (name -> _.asJson)

  /**
   * Attempts to create an entry from a name and an optional value.
   *
   * @tparam A The type of value to encode.
   * @param name   The name of the entry to return.
   * @param option The optional value to encode in the entry.
   * @return The result of attempting to create an entry from a name and an optional value.
   */
  def option[A: Encoder](name: String, option: Option[A]): Option[Entry] =
    Option(option).flatten map (name -> _.asJson)

  /**
   * Attempts to create an entry from a name and a non-empty string.
   *
   * @param name   The name of the entry to return.
   * @param string The non-empty string to encode in the entry.
   * @return The result of attempting to create an entry from a name and a non-empty string.
   */
  def string(name: String, string: String): Option[Entry] =
    Option(string) filterNot (_.isEmpty) map (name -> _.asJson)

  /**
   * Attempts to create an entry from a name and a non-empty sequence of values.
   *
   * @tparam A The type of value to encode.
   * @param name The name of the entry to return.
   * @param seq  The non-empty sequence of values to encode in the entry.
   * @return The result of attempting to create an entry from a name and a non-empty sequence of values.
   */
  def seq[A: Encoder](name: String, seq: Seq[A]): Option[Entry] =
    Option(seq) filterNot (_.isEmpty) map (name -> _.asJson)

  /**
   * Attempts to create an entry from a name and a non-empty collection of entries.
   *
   * @param name   The name of the entry to return.
   * @param collection The non-empty collection of entries to encode in the entry.
   * @return The result of attempting to create an entry from a name and a non-empty collection of entries.
   */
  def collection(name: String, collection: Option[Entry]*): Option[Entry] =
    Option(collection) flatMap (render(_ *)) map (name -> _)

  /**
   * Attempts to create an entry from a named component.
   *
   * @param named The named component to create an entry from.
   * @return The result of attempting to create an entry from a named component.
   */
  def named(named: Named): Option[Entry] =
    Option(named) flatMap (_.render map (named.logicalName -> _))

  /**
   * Attempts to render JSON from a non-empty collection of entries.
   *
   * @param entries The non-empty collection of entries to encode in the entry.
   * @return The result of attempting to render JSON from a non-empty collection of entries.
   */
  def render(entries: Option[Entry]*): Option[Json] =
    Option(entries) map (_.flatten[Entry]) filterNot (_.isEmpty) map (Json.obj(_ *))
