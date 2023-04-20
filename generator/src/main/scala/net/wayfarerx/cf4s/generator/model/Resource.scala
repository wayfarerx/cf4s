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

package net.wayfarerx.cf4s.generator
package model

/**
 * Definition of a resource.
 *
 * @param id            The ID of this resource.
 * @param documentation The link to the documentation for this resource.
 * @param attributes    The attributes of this resource.
 * @param properties    The properties of this resource.
 */
case class Resource(
  id: Id,
  documentation: String,
  attributes: Map[Token, Type.Attribute],
  properties: Map[Token, Property]
)
