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

package github4s.free.algebra

import cats.free.{Free, Inject}
import github4s.GithubResponses._
import github4s.free.domain.{Issue, SearchIssuesResult, SearchParam}

/**
  * Issues ops ADT
  */
sealed trait IssueOp[A]

final case class ListIssues(
    owner: String,
    repo: String,
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[List[Issue]]]

final case class SearchIssues(
    query: String,
    searchParams: List[SearchParam],
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[SearchIssuesResult]]

/**
  * Exposes Issue operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class IssueOps[F[_]](implicit I: Inject[IssueOp, F]) {

  def listIssues(
      owner: String,
      repo: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[List[Issue]]] =
    Free.inject[IssueOp, F](ListIssues(owner, repo, accessToken))

  def searchIssues(
      query: String,
      searchParams: List[SearchParam],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[SearchIssuesResult]] =
    Free.inject[IssueOp, F](SearchIssues(query, searchParams, accessToken))
}

/**
  * Default implicit based DI factory from which instances of the IssueOps may be obtained
  */
object IssueOps {

  implicit def instance[F[_]](implicit I: Inject[IssueOp, F]): IssueOps[F] = new IssueOps[F]

}
