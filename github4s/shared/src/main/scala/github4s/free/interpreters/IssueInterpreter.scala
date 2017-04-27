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
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain._
import github4s.util.URLEncoder
import io.circe.syntax._
import io.circe.generic.auto._
import github4s.free.algebra.IssueOps
import github4s.Config

class IssueInterpreter[C, M[_]](
    implicit urls: GithubApiUrls,
    httpClientImpl: HttpRequestBuilderExtension[C, M])
    extends IssueOps.Handler[M] {

  val httpClient = new HttpClient[C, M]

  /**
   * List issues for a repository
   *
   * Note: In the past, pull requests and issues were more closely aligned than they are now.
   * As far as the API is concerned, every pull request is an issue, but not every issue is a
   * pull request.
   *
   * This endpoint may also return pull requests in the response. If an issue is a pull request,
   * the object will include a `pull_request` key.
   *
   * @param accessToken to identify the authenticated user
   * @param headers     optional user headers to include in the request
   * @param owner       of the repo
   * @param repo        name of the repo
   * @return a GHResponse with the issue list.
   */
  def list(config: Config, owner: String, repo: String): M[GHResponse[List[Issue]]] =
    httpClient.get[List[Issue]](config.accessToken, s"repos/$owner/$repo/issues", config.headers)

  /**
   * Search for issues
   *
   * Note: In the past, pull requests and issues were more closely aligned than they are now.
   * As far as the API is concerned, every pull request is an issue, but not every issue is a
   * pull request.
   *
   * This endpoint may also return pull requests in the response. If an issue is a pull request,
   * the object will include a `pull_request` key.
   *
   * @param accessToken  to identify the authenticated user
   * @param headers      optional user headers to include in the request
   * @param query        the query string for the search
   * @param searchParams list of search params
   * @return a GHResponse with the result of the search.
   */
  def search(
      config: Config,
      query: String,
      searchParams: List[SearchParam]): M[GHResponse[SearchIssuesResult]] = {
    val queryString = s"${URLEncoder.encode(query)}+${searchParams.map(_.value).mkString("+")}"
    httpClient
      .get[SearchIssuesResult](
        config.accessToken,
        "search/issues",
        config.headers,
        Map("q" -> queryString))
  }

  /**
   * Create an issue
   *
   * @param accessToken to identify the authenticated user
   * @param headers     optional user headers to include in the request
   * @param owner       of the repo
   * @param repo        name of the repo
   * @param title       The title of the issue.
   * @param body        The contents of the issue.
   * @param milestone   The number of the milestone to associate this issue with.
   * @param labels      Labels to associate with this issue.
   * @param assignees   Logins for Users to assign to this issue.
   */
  def create(
      config: Config,
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String]): M[GHResponse[Issue]] =
    httpClient.post[Issue](
      config.accessToken,
      s"repos/$owner/$repo/issues",
      config.headers,
      data = NewIssueRequest(title, body, milestone, labels, assignees).asJson.noSpaces)

  /**
   * Edit an issue
   *
   * @param accessToken to identify the authenticated user
   * @param headers     optional user headers to include in the request
   * @param title       The title of the issue.
   * @param body        The contents of the issue.
   * @param state       State of the issue. Either open or closed.
   * @param milestone   The number of the milestone to associate this issue with.
   * @param labels      Labels to associate with this issue.
   *                    Pass one or more Labels to replace the set of Labels on this Issue.
   *                    Send an empty list to clear all Labels from the Issue.
   * @param assignees   Logins for Users to assign to this issue.
   *                    Pass one or more user logins to replace the set of assignees on this Issue.
   *                    Send an empty list to clear all assignees from the Issue.
   */
  def edit(
      config: Config,
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String]): M[GHResponse[Issue]] =
    httpClient.patch[Issue](
      config.accessToken,
      s"repos/$owner/$repo/issues/$issue",
      config.headers,
      data = EditIssueRequest(state, title, body, milestone, labels, assignees).asJson.noSpaces
    )

}
