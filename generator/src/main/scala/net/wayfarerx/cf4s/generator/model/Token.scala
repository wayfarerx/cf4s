/* Token.scala
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

import zio.{Task, ZIO}

/**
 * A token that contains a valid string.
 *
 * @param value The valid string.
 */
final class Token private(val value: String) extends AnyVal:

  /* Return the value of this token. */
  override def toString: String = value

/**
 * Factory for tokens.
 */
object Token:

  /** The pattern that token strings must match. */
  private val Valid = "^([a-zA-Z][a-zA-Z0-9]*)$".r

  /**
   * Attempts to create a token from a string.
   *
   * @param string The string to create a token from.
   * @return The result of attempting to create a token from a string.
   */
  def fromString(string: String): Task[Token] = string match
    case Valid(value) => ZIO succeed Token(value)
    case invalid => ZIO fail new IllegalArgumentException(s"""Invalid token: "$invalid".""")
