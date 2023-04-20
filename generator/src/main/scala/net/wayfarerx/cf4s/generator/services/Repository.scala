/* Repository.scala
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

/**
 * The repository that input specifications are loaded from.
 */
trait Repository:

  /**
   * Load the input specifications with the specified prefix.
   *
   * @param prefix The prefix of the input specifications to load.
   * @return The mapping of specification names to loaded input specifications.
   */
  def load(prefix: String): Task[Map[String, InputSpecification]]

/**
 * Accessor for the environment's repository service.
 */
object Repository:

  /**
   * Load the input specifications with the specified prefix.
   *
   * @param prefix The prefix of the input specifications to load.
   * @return The mapping of specification names to loaded input specifications.
   */
  def load(prefix: String): RIO[Repository, Map[String, InputSpecification]] = ZIO serviceWithZIO (_ load prefix)
