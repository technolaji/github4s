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

package github4s.free.interpreters

import github4s.HttpRequestBuilderExtension
import simulacrum.typeclass
import github4s.GithubDefaultUrls._

import scala.language.higherKinds

@typeclass
trait Capture[M[_]] {
  def capture[A](a: ⇒ A): M[A]
}

class Interpreters[M[_], C](
    implicit C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  implicit val authInterpreter        = new AuthInterpreter[C, M]
  implicit val gistsInterpreter       = new GistInterpreter[C, M]
  implicit val gitDataInterpreter     = new GitDataInterpreter[C, M]
  implicit val issuesInterpreter      = new IssueInterpreter[C, M]
  implicit val pullRequestInterpreter = new PullRequestInterpreter[C, M]
  implicit val repositoryInterpreter  = new RepositoryInterpreter[C, M]
  implicit val statusInterpreter      = new StatusInterpreter[C, M]
  implicit val usersInterpreter       = new UserInterpreter[C, M]
}
