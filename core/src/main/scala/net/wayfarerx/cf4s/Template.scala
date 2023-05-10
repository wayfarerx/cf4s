/* Template.scala
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

case class Template(
  description: String = "",
  parameters: Seq[Parameter[_]] = Seq.empty,
  resources: Seq[Resource] = Seq.empty
)

object Template:

  given Encoder[Template] = Encoder instance { template =>
    Entry.render(
      Entry("AWSTemplateFormatVersion", "2010-09-09"),
      Entry.string("Description", template.description),
      Entry.collection("Parameters", template.parameters map Entry.named *),
      Entry.collection("Resources", template.resources map Entry.named *)
    ).get
  }

  trait Builder:

    protected def description: String = ""

    protected def parameters: Seq[Parameter[_]] = Seq.empty

    protected def resources: Seq[Resource] = Seq.empty

    final def build: Template = Template(description, parameters, resources)
