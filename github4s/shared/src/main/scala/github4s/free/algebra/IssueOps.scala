/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.free.algebra

import cats.free.Free
import freestyle.free.free
import github4s.GithubResponses._
import github4s.free.domain.{Comment, Issue, SearchIssuesResult, SearchParam}

/**
 * Exposes Issue operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object IssueOps {
  @free trait IssueOpsM {

    def listIssues(
        owner: String,
        repo: String,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[List[Issue]]] =
      Free.inject[IssueOp, F](ListIssues(owner, repo, accessToken))

    def getIssue(
        owner: String,
        repo: String,
        number: Int,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[Issue]] =
      Free.inject[IssueOp, F](GetIssue(owner, repo, number, accessToken))

    def searchIssues(
        query: String,
        searchParams: List[SearchParam],
        accessToken: Option[String] = None
    ): Free[F, GHResponse[SearchIssuesResult]] =
      Free.inject[IssueOp, F](SearchIssues(query, searchParams, accessToken))

    def createIssue(
        owner: String,
        repo: String,
        title: String,
        body: String,
        milestone: Option[Int],
        labels: List[String],
        assignees: List[String],
        accessToken: Option[String] = None
    ): Free[F, GHResponse[Issue]] =
      Free.inject[IssueOp, F](
        CreateIssue(owner, repo, title, body, milestone, labels, assignees, accessToken))

    def editIssue(
        owner: String,
        repo: String,
        issue: Int,
        state: String,
        title: String,
        body: String,
        milestone: Option[Int],
        labels: List[String],
        assignees: List[String],
        accessToken: Option[String] = None
    ): Free[F, GHResponse[Issue]] =
      Free.inject[IssueOp, F](
        EditIssue(
          owner,
          repo,
          issue,
          state,
          title,
          body,
          milestone,
          labels,
          assignees,
          accessToken))

    def listComments(
        owner: String,
        repo: String,
        number: Int,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[List[Comment]]] =
      Free.inject[IssueOp, F](ListComments(owner, repo, number, accessToken))

    def createComment(
        owner: String,
        repo: String,
        number: Int,
        body: String,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[Comment]] =
      Free.inject[IssueOp, F](CreateComment(owner, repo, number, body, accessToken))

    def editComment(
        owner: String,
        repo: String,
        id: Int,
        body: String,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[Comment]] =
      Free.inject[IssueOp, F](EditComment(owner, repo, id, body, accessToken))

    def deleteComment(
        owner: String,
        repo: String,
        id: Int,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[Unit]] =
      Free.inject[IssueOp, F](DeleteComment(owner, repo, id, accessToken))
  }

  trait Implicits {}

  object implicits extends Implicits
}
