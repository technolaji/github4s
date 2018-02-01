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

import cats.~>
import freestyle.free._
import github4s.GithubResponses._
import github4s.api.Issues
import github4s.free.adt.IssueOp._
import github4s.free.domain.SearchParam._
import github4s.free.domain.Issue._

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
    ): FS[GHResponse[List[Issue]]]

    def getIssue(
        owner: String,
        repo: String,
        number: Int,
        accessToken: Option[String] = None
    ): FS[GHResponse[Issue]]

    def searchIssues(
        query: String,
        searchParams: List[SearchParam],
        accessToken: Option[String] = None
    ): FS[GHResponse[SearchIssuesResult]]

    def createIssue(
        owner: String,
        repo: String,
        title: String,
        body: String,
        milestone: Option[Int],
        labels: List[String],
        assignees: List[String],
        accessToken: Option[String] = None
    ): FS[GHResponse[Issue]]

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
    ): FS[GHResponse[Issue]]

    def listComments(
        owner: String,
        repo: String,
        number: Int,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[Comment]]]

    def createComment(
        owner: String,
        repo: String,
        number: Int,
        body: String,
        accessToken: Option[String] = None
    ): FS[GHResponse[Comment]]

    def editComment(
        owner: String,
        repo: String,
        id: Int,
        body: String,
        accessToken: Option[String] = None
    ): FS[GHResponse[Comment]]

    def deleteComment(
        owner: String,
        repo: String,
        id: Int,
        accessToken: Option[String] = None
    ): FS[GHResponse[Unit]]
  }

  trait Implicits {

    /**
     * Lifts Issue Ops to an effect capturing Monad such as Task via natural transformations
     */
    def issueOpsInterpreter: IssueOp ~> K =
      new (IssueOp ~> K) {

        val issues = new Issues()

        def apply[A](fa: IssueOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case ListIssues(owner, repo, accessToken) ⇒
              issues.list(accessToken, headers, owner, repo)
            case GetIssue(owner, repo, number, accessToken) ⇒
              issues.get(accessToken, headers, owner, repo, number)
            case SearchIssues(query, searchParams, accessToken) ⇒
              issues.search(accessToken, headers, query, searchParams)
            case CreateIssue(owner, repo, title, body, milestone, labels, assignees, accessToken) ⇒
              issues
                .create(
                  accessToken,
                  headers,
                  owner,
                  repo,
                  title,
                  body,
                  milestone,
                  labels,
                  assignees)
            case EditIssue(
                owner,
                repo,
                issue,
                state,
                title,
                body,
                milestone,
                labels,
                assignees,
                accessToken) ⇒
              issues.edit(
                accessToken,
                headers,
                owner,
                repo,
                issue,
                state,
                title,
                body,
                milestone,
                labels,
                assignees)
            case ListComments(owner, repo, number, accessToken) ⇒
              issues.listComments(accessToken, headers, owner, repo, number)
            case CreateComment(owner, repo, number, body, accessToken) ⇒
              issues.createComment(accessToken, headers, owner, repo, number, body)
            case EditComment(owner, repo, id, body, accessToken) ⇒
              issues.editComment(accessToken, headers, owner, repo, id, body)
            case DeleteComment(owner, repo, id, accessToken) ⇒
              issues.deleteComment(accessToken, headers, owner, repo, id)
          }
        }
      }
  }

  object implicits extends Implicits
}
