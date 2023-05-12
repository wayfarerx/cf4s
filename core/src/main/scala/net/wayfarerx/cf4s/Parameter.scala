/* Parameter.scala
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
 * Represents a parameter that can be configured when creating or updating a CloudFormation stack.
 *
 * @tparam A The type of data this parameter accepts.
 * @param logicalName           The logical name of this parameter in the template.
 * @param description           A string of up to 4000 characters that describes this parameter.
 * @param default               A value of the appropriate type to use if no value is specified when a stack is created.
 * @param allowedValues         An array containing the list of values allowed for this parameter.
 * @param allowedPattern        A regular expression that represents the patterns to allow for String or Strings types.
 * @param minValue              The smallest numeric value you want to allow for Number types.
 * @param maxValue              The largest numeric value you want to allow for Number types.
 * @param minLength             The smallest number of characters you want to allow for String types.
 * @param maxLength             The largest number of characters you want to allow for String types.
 * @param constraintDescription A string that explains a constraint when the constraint is violated.
 * @param noEcho                Whether to mask the parameter value to prevent it from being displayed.
 */
case class Parameter[A: Encoder : Parameter.Type](
  logicalName: String,
  description: String = "",
  default: Option[A] = None,
  allowedValues: Seq[A] = Seq.empty,
  allowedPattern: String = "",
  minValue: Option[A] = None,
  maxValue: Option[A] = None,
  minLength: Option[Int] = None,
  maxLength: Option[Int] = None,
  constraintDescription: String = "",
  noEcho: Option[Boolean] = None
) extends Named:

  /* Attempt to render the content of this parameter. */
  override def render: Option[Json] = Entry.render(
    Entry("Type", summon[Parameter.Type[A]]),
    Entry.string("Description", description),
    Entry.option("Default", default),
    Entry.seq("AllowedValues", allowedValues),
    Entry.string("AllowedPattern", allowedPattern),
    Entry.option("MinValue", minValue),
    Entry.option("MaxValue", maxValue),
    Entry.option("MinLength", minLength),
    Entry.option("MaxLength", maxLength),
    Entry.string("ConstraintDescription", constraintDescription),
    Entry.option("NoEcho", noEcho)
  )

/**
 * Definitions associated with template parameters.
 */
object Parameter:

  /**
   * The given conversion of typed parameters to similarly typed data.
   *
   * @tparam A The type of the parameter.
   * @return The given conversion of typed parameters to similarly typed data.
   */
  given ParameterAsData[A]: Conversion[Parameter[A], Data[A]] = Fn ref _.logicalName

  /**
   * Base type for supported parameter types.
   *
   * @tparam A The runtime type associated with this parameter type.
   */
  sealed trait Type[A]:

    /** The name of this parameter type. */
    def name: String

  /**
   * Definitions associated with parameter types.
   */
  object Type:

    /**
     * Returns the given encoder for the specified parameter type.
     *
     * @tparam A The runtime type associated with the parameter type.
     * @return The given encoder for the specified parameter type.
     */
    given TypeEncoder[A]: Encoder[Type[A]] = Encoder.encodeString contramap (_.name)

    /**
     * Returns the given parameter type for single numbers.
     *
     * @tparam A The type of number to represent.
     * @return The given parameter type for single numbers.
     */
    given NumberType[A: Numeric]: Type[A] = Definition("Number")

    /**
     * Returns the given parameter type for sequences of numbers.
     *
     * @tparam A The type of number to represent.
     * @return The given parameter type for sequences of numbers.
     */
    given NumberListType[A: Numeric]: Type[Seq[A]] = Definition("List<Number>")

    /**
     * Returns the given parameter type for single strings.
     *
     * @return The given parameter type for single strings.
     */
    given Type[String] = Definition("String")

    /**
     * Returns the given parameter type for sequences of strings.
     *
     * @return The given parameter type for sequences of strings.
     */
    given Type[Seq[String]] = Definition("CommaDelimitedList")

    /**
     * Implementation of a parameter type.
     *
     * @tparam A The runtime type associated with this parameter type.
     * @param name The name of this parameter type.
     */
    private final case class Definition[A](name: String) extends Type[A]
