/* Composer.scala
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
package services

import zio.{RIO, Task, ZIO}

import model.*
import code.*

/**
 * The service that composes classes from model objects.
 */
trait Composer:

  /**
   * Composes classes derived from the specified resources.
   *
   * @param resources The resources to compose case classes from.
   * @return Case classes derived from the specified resources.
   */
  def compose(resources: Seq[Resource]): Task[Seq[CodeClass]]

/**
 * Accessor for the environment's composer service.
 */
object Composer:

  /**
   * Composes classes derived from the specified resources.
   *
   * @param resources   The resources to compose case classes from.
   * @return Case classes derived from the specified resources.
   */
  def compose(resources: Seq[Resource]): RIO[Composer, Seq[CodeClass]] = ZIO serviceWithZIO (_ compose resources)
