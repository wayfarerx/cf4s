/* State.scala
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

import scala.annotation.tailrec

import cats.data.StateT

import zio.{Task, ZIO}
import zio.interop.catz.*

/** A task effect type that manages state data. */
private[generator] type State[S, A] = StateT[Task, S, A]

/**
 * Factory for effect types that manages state data.
 */
private[generator] object State:

  /**
   * Creates a state by applying the specified function to the current state.
   *
   * @tparam S The type of state data being managed.
   * @tparam A The result of this operation.
   * @param f The function that evaluates against the current state.
   * @return A state after applying the specified function to the current state.
   */
  def apply[S, A](f: S => Task[(S, A)]): State[S, A] = StateT apply f

  /**
   * Creates a state that returns the current state data.
   *
   * @tparam S The type of state data being managed.
   * @return A state that returns the current state data.
   */
  def get[S]: State[S, S] = StateT inspect identity

  /**
   * Creates a state that always succeeds with the specified value.
   *
   * @tparam S The type of state data being managed.
   * @tparam A The type of the value to return.
   * @param value The value to return.
   * @return A state that always succeeds with the specified value.
   */
  def succeed[S, A](value: A): State[S, A] = StateT liftF ZIO.succeed(value)

  /**
   * Creates a state that always fails.
   *
   * @tparam S The type of state data being managed.
   * @param thrown The failure that was thrown.
   * @return A state that always fails.
   */
  def fail[S](thrown: Throwable): State[S, Nothing] = StateT liftF ZIO.fail(thrown)

  /**
   * Creates a state that evaluates the specified task.
   *
   * @tparam S The type of state data being managed.
   * @tparam A The type of the result.
   * @param task The task that provides the result.
   * @return A state that evaluates the specified task.
   */
  def lift[S, A](task: Task[A]): State[S, A] = StateT liftF task

  /**
   * Creates a state that derives a result from the current state data.
   *
   * @tparam S The type of state data being managed.
   * @tparam A The type of the result.
   * @param f The function that derives a result from the current state data.
   * @return A state that derives a result from the current state data.
   */
  def inspect[S, A](f: S => A): State[S, A] = StateT inspect f

  /**
   * Creates a state that derives a result from the current state data.
   *
   * @tparam S The type of state data being managed.
   * @tparam A The type of the result.
   * @param f The function that derives a result from the current state data.
   * @return A state that derives a result from the current state data.
   */
  def inspectF[S, A](f: S => Task[A]): State[S, A] = StateT inspectF f

  /**
   * Creates a state that modifies the current state data.
   *
   * @tparam S The type of state data being managed.
   * @param f The function that modifies the current state data.
   * @return A state that modifies the current state data.
   */
  def modify[S](f: S => S): State[S, Unit] = StateT modify f

  /**
   * Creates a state that modifies the current state data.
   *
   * @tparam S The type of state data being managed.
   * @param f The function that modifies the current state data.
   * @return A state that modifies the current state data.
   */
  def modifyF[S](f: S => Task[S]): State[S, Unit] = StateT modifyF f

  /**
   * Folds a state over an iterable sequence of items.
   *
   * @tparam S The type of state data being managed.
   * @tparam I The type of the input items.
   * @tparam O The type of the result.
   * @param input The input items.
   * @param zero The initial result state.
   * @param f The function that folds input items into the result.
   * @return A state folded an iterable sequence of items.
   */
  def foldLeft[S, I, O](input: => Iterable[I])(zero: => O)(f: (O, I) => State[S, O]): State[S, O] =

    def foldingLeft(iterator: Iterator[I], accumulator: State[S, O]): State[S, O] =
      if iterator.hasNext then
        for
          accumulated <- accumulator
          result <- foldingLeft(iterator, f(accumulated, iterator.next))
        yield result
      else accumulator

    foldingLeft(input.iterator, succeed(zero))
