/* Named.scala
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

/**
 * Base type for named components in a template.
 */
trait Named:

  /** The logical name of this component. */
  def logicalName: String

  /**
   * Attempts to render the content of this named component.
   *
   * @return The result of attempting to render content of this named component.
   */
  def render: Option[Json]
