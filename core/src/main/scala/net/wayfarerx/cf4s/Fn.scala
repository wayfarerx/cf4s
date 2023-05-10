/* Fn.scala
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
 * The intrinsic functions provided by AWS CloudFormation.
 */
object Fn:

  /**
   * Returns the value of an attribute from a resource in the template.
   *
   * @tparam A The type of attribute data to return.
   * @param logicalName   The logical name of the resource that contains the attribute that you want.
   * @param attributeName The name of the resource-specific attribute whose value you want.
   * @return The value of an attribute from a resource in the template.
   */
  def getAtt[A](logicalName: String, attributeName: Data[String]): Data[A] =
    Json obj "Fn::GetAtt" -> Json.arr(logicalName.asJson, attributeName.asJson)

  /**
   * Appends a set of values into a single value, separated by the specified delimiter.
   *
   * @param delimiter The value you want to occur between fragments.
   * @param values    The list of values you want combined.
   * @return A set of values joined into a single value, separated by the specified delimiter.
   */
  def join(delimiter: String, values: Data[Seq[Data[String]]]): Data[String] =
    Json obj "Fn::Join" -> Json.arr(delimiter.asJson, values.asJson)

  /**
   * Returns a single object from a list of objects by index.
   *
   * @tparam A The type of data to select.
   * @param index   The index of the object to retrieve.
   * @param objects The list of objects to select from.
   * @return A single object from a list of objects by index.
   */
  def select[A: Encoder](index: Data[Int], objects: Data[Seq[Data[A]]]): Data[A] =
    Json obj "Fn::Select" -> Json.arr(index.asJson, objects.asJson)

  /**
   * Split a string into a list of string values
   *
   * @param delimiter A string value that determines where the source string is divided.
   * @param source    The string value that you want to split.
   * @return The resulting list of values.
   */
  def split(delimiter: String, source: Data[String]): Data[Seq[String]] =
    Json obj "Fn::Split" -> Json.arr(delimiter.asJson, source.asJson)

  /**
   * Substitutes variables in an input string with values that you specify.
   *
   * @param string    A string with variables that AWS CloudFormation substitutes with their associated values at runtime.
   * @param variables The names and values of the variables to substitute in the input string.
   * @return The input string with values that you specify substituted for variables.
   */
  def sub(string: String, variables: (String, Data[String])*): Data[String] =
    Json obj "Fn::Sub" -> {
      if variables.isEmpty then string.asJson
      else Json.arr(string.asJson, Json.obj(variables.map(_ -> _.asJson) *))
    }

  /**
   * Returns the value of the specified parameter or resource.
   *
   * @tparam A The type of reference data to return.
   * @param logicalName The logical name of the resource or parameter you want to reference.
   * @return The value of the specified parameter or resource.
   */
  def ref[A](logicalName: String): Data[A] =
    Json obj "Ref" -> logicalName.asJson
