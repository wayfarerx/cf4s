/* RepositoryLive.scala
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

import java.nio.charset.StandardCharsets
import java.util.zip.{ZipEntry, ZipInputStream}

import io.circe.parser.parse

import zio.{Task, URLayer, ZIO, ZLayer}

import input.InputSpecification

/**
 * The implementation of the repository that CloudFormation specification definitions are loaded from.
 *
 * @param configuration The configuration service to use.
 */
case class RepositoryLive(configuration: Configuration) extends Repository:

  /** Access to the data this repository uses. */
  private val data = for
    result <- ZIO.acquireRelease {
      for
        specificationsUrl <- configuration.specificationsUrl
        stream <- ZIO attempt new ZipInputStream(specificationsUrl.openStream)
      yield stream
    }(stream => ZIO.attempt(stream.close()).orDie)
  yield result

  /* Load the CloudFormation specifications with the specified prefix. */
  override def load(prefix: String): Task[Map[String, InputSpecification]] = ZIO scoped {
    for
      entries <- data
      result <- RepositoryLive.load(entries) { entry =>
        val name = entry.getName
        if name.startsWith(prefix) && name.endsWith(`.json`) then
          for
            text <- ZIO attempt new String(entries.readAllBytes, StandardCharsets.UTF_8)
            json <- ZIO fromEither parse(text)
            result <- ZIO fromEither json.as[InputSpecification]
          yield Some(name.dropRight(`.json`.length) -> result)
        else ZIO.none
      }
    yield result
  }

/**
 * Factory for repository service implementations.
 */
object RepositoryLive:

  /** The layer that creates repository service implementations. */
  val layer: URLayer[Configuration, Repository] = ZLayer {
    for configuration <- ZIO.service[Configuration] yield RepositoryLive(configuration)
  }

  /**
   * Processes every remaining element of a ZIP input stream.
   *
   * @param entries The ZIP input stream to process.
   * @param f       The function to process ZIP entries with.
   * @return The specifications processed from the ZIP input stream.
   */
  private def load(entries: ZipInputStream)(
    f: ZipEntry => Task[Option[(String, InputSpecification)]]
  ): Task[Map[String, InputSpecification]] = for
    entry <- ZIO attempt Option(entries.getNextEntry)
    result <- entry.fold(ZIO.succeed(Map.empty)) { zipEntry =>
      for
        current <- f(zipEntry)
        remaining <- load(entries)(f)
      yield current.fold(remaining)(remaining + _)
    }
  yield result
