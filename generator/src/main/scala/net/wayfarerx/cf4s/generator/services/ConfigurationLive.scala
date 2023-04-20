/* ConfigurationLive.scala
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

import zio.{UIO, ULayer, ZIO, ZLayer}

import java.net.URL

/**
 * Implementation of the configuration service.
 *
 * @param _specificationsUrl The URL where the specification ZIP file can be found.
 */
case class ConfigurationLive(_specificationsUrl: URL) extends Configuration:

  /* Return the URL where the specification ZIP file can be found. */
  override val specificationsUrl: UIO[URL] = ZIO succeed _specificationsUrl

/**
 * Factory for configuration service implementations.
 */
object ConfigurationLive:

  /**
   * Returns a layer that creates configuration service implementations.
   *
   * @param specificationsUrl The URL where the specification ZIP file can be found.
   * @return A layer that creates configuration service implementations.
   */
  def layer(specificationsUrl: URL): ULayer[Configuration] = ZLayer succeed ConfigurationLive(specificationsUrl)
