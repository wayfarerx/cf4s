/* Resource.scala
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

/**
 * Base type for CloudFormation resources.
 */
trait Resource extends Named:

  /**
   * Returns the fully-qualified name of the type of this resource.
   *
   * @return The fully-qualified name of the type of this resource.
   */
  def resourceType: String

  /**
   * Returns the properties defined for this resource.
   *
   * @return The properties defined for this resource.
   */
  def resourceProperties: Option[Json]

  /* Attempt to render the content of this resource. */
  final override def render: Option[Json] = Entry.render(
    Entry("Type", resourceType),
    Entry.option("Properties", resourceProperties)
  )

/**
 * Definitions associated with resources.
 */
object Resource:

  /**
   * Returns the encoder for references to the specified resource.
   *
   * @tparam A The type of resource to reference.
   * @return The encoder for references to the specified resource.
   */
  given ResourceEncoder[A <: Resource]: Encoder[A] = Fn ref _.logicalName
