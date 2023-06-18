/* CodeField.scala
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
package code

/**
 * Definition of a field that has CloudFormation metadata.
 *
 * @param name The capitalized name of this field.
 * @param fieldType The type of this field.
 * @param required True if this field is required.
 * @param documentation The optional documentation link for this field.
 */
case class CodeField(
  name: String,
  fieldType: String,
  required: Boolean = false,
  documentation: Option[String] = None
):

  /** The name of this field in code. */
  lazy val fieldName: String = decapitalize(name)
