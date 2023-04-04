/* Name.scala
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

import zio.Task

/**
 * The name of a property.
 *
 * @param qualifier The qualifier of this name.
 * @param value     The value of this name.
 */
case class Name(qualifier: Option[Id], value: Token):

  /* Render this name as a string. */
  override def toString: String = qualifier.fold(value.toString)(_.toString + Name.Dot + value)

/**
 * Factory for names.
 */
object Name:

  /** The dot character. */
  private val Dot = "."

  /**
   * Attempts to create a name from a string.
   *
   * @param string The string to create a name from.
   * @return The result of attempting to create a name from a string.
   */
  def fromString(string: String): Task[Name] = string lastIndexOf Dot match
    case index if index >= 0 =>
      for
        qualifier <- Id fromString string.substring(0, index)
        value <- Token fromString string.substring(index + 1)
      yield Name(Some(qualifier), value)
    case _ => Token fromString string map (Name(None, _))
