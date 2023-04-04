/* Id.scala
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

import cats.data.NonEmptySeq

import zio.{Task, ZIO}


/**
 * The ID of a resource.
 *
 * @param tokens The the tokens that constitute this ID.
 */
case class Id(tokens: NonEmptySeq[Token]):

  /* Render this ID as a string. */
  override def toString: String = tokens.iterator mkString Id.Delimiter

/**
 * Factory for IDs.
 */
object Id:

  /** The delimiter for ID tokens. */
  private val Delimiter = "::"

  /**
   * Attempts to create an ID from a string.
   *
   * @param string The string to create an ID from.
   * @return The result of attempting to create an ID from a string.
   */
  def fromString(string: String): Task[Id] = for
    tokens <- ZIO.foldLeft(string split Delimiter filterNot (_.isEmpty))(Seq.empty[Token]) { (prefix, token) =>
      Token fromString token map (prefix :+ _)
    }
    id <- NonEmptySeq.fromSeq(tokens).fold(ZIO fail IllegalArgumentException(s"Invalid ID: $string."))(ZIO.succeed)
  yield Id(id)
