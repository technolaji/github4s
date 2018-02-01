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
import github4s.api.PullRequests
import github4s.free.adt.PullRequestOp._
import github4s.free.domain.PullRequest._

/**
 * Exposes Pull Request operations as a Free monadic algebra that may be combined with other
 * Algebras via Coproduct
 */
object PullRequestOps {

  @free trait PullRequestOpsM {

    def listPullRequests(
        owner: String,
        repo: String,
        filters: List[PRFilter] = Nil,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[PullRequest]]]

    def listPullRequestFiles(
        owner: String,
        repo: String,
        number: Int,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[PullRequestFile]]]

    def createPullRequest(
        owner: String,
        repo: String,
        newPullRequest: NewPullRequest,
        head: String,
        base: String,
        maintainerCanModify: Option[Boolean] = Some(true),
        accessToken: Option[String] = None
    ): FS[GHResponse[PullRequest]]

    def listPullRequestReviews(
        owner: String,
        repo: String,
        pullRequest: Int,
        accessToken: Option[String] = None): FS[GHResponse[List[PullRequestReview]]]

    def getPullRequestReview(
        owner: String,
        repo: String,
        pullRequest: Int,
        review: Int,
        accessToken: Option[String] = None): FS[GHResponse[PullRequestReview]]
  }

  trait Implicits {

    /**
     * Lifts PullRequest Ops to an effect capturing Monad such as Task via natural transformations
     */
    def pullRequestOpsInterpreter: PullRequestOp ~> K =
      new (PullRequestOp ~> K) {

        val pullRequests = new PullRequests()

        def apply[A](fa: PullRequestOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case ListPullRequests(owner, repo, filters, accessToken) ⇒
              pullRequests.list(accessToken, headers, owner, repo, filters)
            case ListPullRequestFiles(owner, repo, number, accessToken) ⇒
              pullRequests.listFiles(accessToken, headers, owner, repo, number)
            case CreatePullRequest(
                owner,
                repo,
                newPullRequest,
                head,
                base,
                maintainerCanModify,
                accessToken) ⇒
              pullRequests
                .create(
                  accessToken,
                  headers,
                  owner,
                  repo,
                  newPullRequest,
                  head,
                  base,
                  maintainerCanModify)
            case ListPullRequestReviews(owner, repo, pullRequest, accessToken) ⇒
              pullRequests.listReviews(accessToken, headers, owner, repo, pullRequest)
            case GetPullRequestReview(owner, repo, pullRequest, review, accessToken) ⇒
              pullRequests.getReview(accessToken, headers, owner, repo, pullRequest, review)
          }
        }
      }

  }

  object implicits extends Implicits
}
