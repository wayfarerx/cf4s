/* Linker.scala
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

import input.InputSpecification
import model.Resource

/**
 * The service that links input specifications into sequences of resources.
 */
trait Linker:

  /**
   * Links the specified input specification into a collection of resources.
   *
   * @param specification The input specification to link.
   * @return The linked collection of resources.
   */
  def link(specification: InputSpecification): Task[Seq[Resource]]

/**
 * Accessor for the environment's linker service.
 */
object Linker:

  /**
   * Links the specified input specification into a sequence of resources.
   *
   * @param specification The input specification to link.
   * @return The linked sequence of resources.
   */
  def link(specification: InputSpecification): RIO[Linker, Seq[Resource]] = ZIO serviceWithZIO (_ link specification)
