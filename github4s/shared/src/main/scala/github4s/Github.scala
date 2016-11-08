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
import cats.{Monad, RecursiveTailRecM}
import scala.concurrent.ExecutionContext
import cats.arrow.FunctionK
import github4s.GithubResponses._
import github4s.app._
import github4s.free.interpreters._
import cats.free.Free
import cats.instances.future._

import scala.concurrent.Future

case class Config(accessToken: Option[String] = None,
                  headers: Map[String, String] = Map.empty[String, String])

object Config {
  val empty = Config()
}

object Github {

  private[this] val gh: GitHub4s[GitHub4s.T] = GitHub4s[GitHub4s.T]

  val users = gh.userOps
  val repos = gh.repositoryOps
  val auth  = gh.authOps
  val gists = gh.gistOps

  implicit class GithubIOSyntaxM[A](gio: Free[GitHub4s.T, GHResponse[A]]) {

    def execK[M[_]: Monad: RecursiveTailRecM, C](
        implicit I: Interpreters[M, C],
        H: HttpRequestBuilderExtension[C, M]): I.K[GHResponse[A]] = {
      import I._
      import io.freestyle.syntax._
      gio.foldMap[I.K](implicitly[FunctionK[GitHub4s.T, I.K]])
    }

    def exec[M[_]: Monad: RecursiveTailRecM, C](config: Config = Config.empty)(
        implicit I: Interpreters[M, C],
        H: HttpRequestBuilderExtension[C, M]): M[GHResponse[A]] =
      execK.run(config)

    def execFuture[C](config: Config = Config.empty)(implicit I: Interpreters[Future, C],
                                                     H: HttpRequestBuilderExtension[C, Future],
                                                     ec: ExecutionContext): Future[GHResponse[A]] =
      exec[Future, C](config)

  }

  implicit class GithubIOSyntaxEither[F[_], A](gio: Free[F, GHResponse[A]]) {
    def liftGH: EitherT[Free[F, ?], GHException, GHResult[A]] =
      EitherT[Free[F, ?], GHException, GHResult[A]](gio)
  }

}
