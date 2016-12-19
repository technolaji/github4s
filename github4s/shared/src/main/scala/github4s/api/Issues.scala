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

package github4s.api

import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain.{Issue, SearchIssuesResult, SearchParam}
import github4s.free.interpreters.Capture
import github4s.util.URLEncoder
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Issues operations  */
class Issues[C, M[_]](implicit urls: GithubApiUrls,
                      C: Capture[M],
                      httpClientImpl: HttpRequestBuilderExtension[C, M]) {

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
    * @param headers optional user headers to include in the request
    * @param owner of the repo
    * @param repo name of the repo
    * @return a GHResponse with the issue list.
    */
  def list(accessToken: Option[String] = None,
           headers: Map[String, String] = Map(),
           owner: String,
           repo: String): M[GHResponse[List[Issue]]] =
    httpClient.get[List[Issue]](accessToken, s"repos/$owner/$repo/issues", headers)

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
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @param searchParams list of search params
    * @return a GHResponse with the result of the search.
    */
  def search(accessToken: Option[String] = None,
             headers: Map[String, String] = Map(),
             query: String,
             searchParams: List[SearchParam]): M[GHResponse[SearchIssuesResult]] = {
    val queryString = s"search/issues?q=$query+${searchParams.map(_.value).mkString("+")}"
    httpClient.get[SearchIssuesResult](accessToken,
                                       s"search/issues?q=${URLEncoder.encode(queryString)}",
                                       headers)
  }

}
