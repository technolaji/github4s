/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s

import cats.data.{EitherT, Kleisli, OptionT}
import cats.{MonadError, RecursiveTailRecM, ~>}
import cats.arrow.FunctionK
import cats.free.Inject._
import cats.implicits._
import github4s.GithubResponses._
import github4s.app._
import github4s.free.interpreters.Interpreters
import cats.free.Free

import scala.concurrent.Future

case class Config(accessToken: Option[String] = None,
                  headers: Map[String, String] = Map.empty[String, String])

object Config {
  val empty = Config()
}

object Github {

  import io.freestyle.syntax._

  private[this] val gh: GitHub4s[GitHub4s.T] = GitHub4s[GitHub4s.T]

  val users = gh.userOps
  val repos = gh.repositoryOps
  val auth  = gh.authOps
  val gists = gh.gistOps

  implicit class GithubIOSyntaxEither[F[_], A](gio: Free[F, GHResponse[A]]) {

    type GHIO[A] = Free[F, A]

    def execK[M[_], C](implicit I: Interpreters[M, C],
                       A: MonadError[M, Throwable],
                       TR: RecursiveTailRecM[M],
                       H: HttpRequestBuilderExtension[C, M]): Kleisli[M, Config, GHResponse[A]] = {
      import I._
      type K[A] = Kleisli[M, Config, A]
      gio.foldMap(implicitly[FunctionK[F, K]])
    }

    def exec[M[_], C](config: Config = Config.empty)(
        implicit I: Interpreters[M, C],
        A: MonadError[M, Throwable],
        TR: RecursiveTailRecM[M],
        H: HttpRequestBuilderExtension[C, M]): M[GHResponse[A]] =
      execK.run(config)

    def execFuture[C](config: Config = Config.empty)(
        implicit I: Interpreters[Future, C],
        ec: scala.concurrent.ExecutionContext,
        A: MonadError[Future, Throwable],
        TR: RecursiveTailRecM[Future],
        H: HttpRequestBuilderExtension[C, Future]): Future[GHResponse[A]] =
      exec[Future, C](config)

    def liftGH: EitherT[GHIO, GHException, GHResult[A]] =
      EitherT[GHIO, GHException, GHResult[A]](gio)

  }

}
