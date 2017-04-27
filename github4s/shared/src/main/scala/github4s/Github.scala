/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s

import cats.data.{EitherT, Kleisli}
import github4s.GithubResponses._

import scala.concurrent.Future
import scala.language.higherKinds
import freestyle._
import freestyle.implicits._
import github4s.app._

/**
 * Represent the Github API wrapper
 * @param accessToken to identify the authenticated user
 */
class Github(config: Config) {

  lazy val users        = new GHUsers[GitHub4s.Op]
  lazy val repos        = new GHRepos[GitHub4s.Op]
  lazy val auth         = new GHAuth[GitHub4s.Op]
  lazy val gists        = new GHGists[GitHub4s.Op]
  lazy val issues       = new GHIssues[GitHub4s.Op]
  lazy val gitData      = new GHGitData[GitHub4s.Op]
  lazy val pullRequests = new GHPullRequests[GitHub4s.Op]
  lazy val statuses     = new GHStatuses[GitHub4s.Op]

}

/** Companion object for [[github4s.Github]] */
object Github {

  def apply(config: Config) = new Github(config)

  implicit class GithubIOSyntaxEither[A](gio: FreeS[GitHub4s.Op, GHResponse[A]]) {
    def execK[M[_], C]: Kleisli[M, Config, GHResponse[A]] =
      gio.foldMap(implicitly[FSHandler[GitHub4s.Op, M]])
    def exec[M[_], C](config: Config): M[GHResponse[A]] =
      execK.run(config)

    def execFuture[C](config: Config): Future[GHResponse[A]] =
      exec[Future, C](config)

    def liftGH: EitherT[FreeS[GitHub4s.Op, ?], GHException, GHResult[A]] =
      EitherT[FreeS[GitHub4s.Op, ?], GHException, GHResult[A]](gio)

  }
}
case class Config(accessToken: Option[String] = None, headers: Map[String, String] = Map.empty)

object Config {
  val empty = Config()
}
