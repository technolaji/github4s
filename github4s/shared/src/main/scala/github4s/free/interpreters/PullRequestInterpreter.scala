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

import github4s.GithubResponses.GHResponse
import github4s.free.domain._
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import io.circe.generic.auto._
import github4s.free.algebra.PullRequestOps
import github4s.Config

import scala.language.higherKinds

class PullRequestInterpreter[C, M[_]](
    implicit urls: GithubApiUrls,
    httpClientImpl: HttpRequestBuilderExtension[C, M])
    extends PullRequestOps.Handler[M] {

  val httpClient = new HttpClient[C, M]

  /**
   * List pull requests for a repository
   *
   * @param accessToken to identify the authenticated user
   * @param headers     optional user headers to include in the request
   * @param owner       of the repo
   * @param repo        name of the repo
   * @param filters     define the filter list. Options are:
   *   - state: Either `open`, `closed`, or `all` to filter by state. Default: `open`
   *   - head: Filter pulls by head user and branch name in the format of `user:ref-name`.
   *                    Example: `github:new-script-format`.
   *   - base: Filter pulls by base branch name. Example: `gh-pages`.
   *   - sort: What to sort results by. Can be either `created`, `updated`, `popularity` (comment count)
   *                    or `long-running` (age, filtering by pulls updated in the last month). Default: `created`
   *   - direction: The direction of the sort. Can be either `asc` or `desc`.
   *                    Default: `desc` when sort is created or sort is not specified, otherwise `asc`.
   * @return a GHResponse with the pull request list.
   */
  def list(
      config: Config,
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil): M[GHResponse[List[PullRequest]]] =
    httpClient.get[List[PullRequest]](
      config.accessToken,
      s"repos/$owner/$repo/pulls",
      config.headers,
      filters.map(_.tupled).toMap)

  /**
   * List files for a specific pull request
   *
   * @param accessToken to identify the authenticated user
   * @param headers     optional user headers to include in the request
   * @param owner       of the repo
   * @param repo        name of the repo
   * @param number      of the pull request for which we want to list the files
   * @return a GHResponse with the list of files affected by the pull request identified by number.
   */
  def listFiles(
      config: Config,
      owner: String,
      repo: String,
      number: Int): M[GHResponse[List[PullRequestFile]]] =
    httpClient
      .get[List[PullRequestFile]](
        config.accessToken,
        s"repos/$owner/$repo/pulls/$number/files",
        config.headers)
}
