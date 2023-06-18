/* CodeClass.scala
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
 * Represents a class that will be generated.
 *
 * @param name          The name of this class.
 * @param resourceType  The optional type of resource this class represents.
 * @param documentation The optional documentation link for this class.
 * @param fields        The fields this class contains.
 * @param methods       The methods this class provides.
 * @param companion     The optional companion object for this class.
 */
case class CodeClass(
  name: String,
  resourceType: Option[String] = None,
  documentation: Option[String] = None,
  fields: Seq[CodeField] = Seq.empty,
  methods: Seq[CodeMethod] = Seq.empty,
  companion: Option[CodeObject] = None
)
