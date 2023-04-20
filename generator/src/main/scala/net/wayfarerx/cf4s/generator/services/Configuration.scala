/* Configuration.scala
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

import java.net.URL

import zio.{UIO, URIO, ZIO}

/**
 * The configuration service.
 */
trait Configuration:

  /** The URL where the specification ZIP file can be found. */
  def specificationsUrl: UIO[URL]

/**
 * Accessor for the environment's configuration service.
 */
object Configuration:

  /** The URL where the specification ZIP file can be found. */
  def specificationsUrl: URIO[Configuration, URL] = ZIO serviceWithZIO (_.specificationsUrl)
