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
package output

/**
 * Base type for classes that will be generated.
 */
sealed trait CodeClass:

  /** The name of the generated class. */
  def name: String

  /** The fields this class contains. */
  def fields: Seq[CodeField]

  /** The optional documentation link for this class. */
  def documentation: Option[String]

/**
 * Factory for code classes.
 */
object CodeClass:

  /**
   * A property class.
   *
   * @param name          The name of the generated class.
   * @param fields        The fields the class contains.
   * @param documentation The optional documentation link for the class.
   * @return The specified property class.
   */
  case class Property(
    name: String,
    fields: Seq[CodeField] = Seq.empty,
    documentation: Option[String] = None
  ) extends CodeClass

  /**
   * A resource class.
   *
   * @param name          The name of the generated class.
   * @param _type         The CloudFormation type of the class.
   * @param fields        The fields the class contains.
   * @param methods       The methods the class provides.
   * @param companion     The optional companion object for the class.
   * @param documentation The optional documentation link for the class.
   * @return The specified resource class.
   */
  case class Resource(
    name: String,
    _type: String,
    fields: Seq[CodeField] = Seq.empty,
    methods: Seq[CodeMethod] = Seq.empty,
    companion: Option[CodeObject] = None,
    documentation: Option[String] = None
  ) extends CodeClass
