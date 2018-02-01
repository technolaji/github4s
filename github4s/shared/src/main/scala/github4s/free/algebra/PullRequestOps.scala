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
import github4s.free.domain._

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
    ): Free[F, GHResponse[List[PullRequest]]] =
      Free.inject[PullRequestOp, F](ListPullRequests(owner, repo, filters, accessToken))

    def listPullRequestFiles(
        owner: String,
        repo: String,
        number: Int,
        accessToken: Option[String] = None
    ): Free[F, GHResponse[List[PullRequestFile]]] =
      Free.inject[PullRequestOp, F](ListPullRequestFiles(owner, repo, number, accessToken))

    def createPullRequest(
        owner: String,
        repo: String,
        newPullRequest: NewPullRequest,
        head: String,
        base: String,
        maintainerCanModify: Option[Boolean] = Some(true),
        accessToken: Option[String] = None
    ): Free[F, GHResponse[PullRequest]] =
      Free.inject[PullRequestOp, F](
        CreatePullRequest(
          owner,
          repo,
          newPullRequest,
          head,
          base,
          maintainerCanModify,
          accessToken))

    def listPullRequestReviews(
        owner: String,
        repo: String,
        pullRequest: Int,
        accessToken: Option[String] = None): Free[F, GHResponse[List[PullRequestReview]]] =
      Free.inject[PullRequestOp, F](ListPullRequestReviews(owner, repo, pullRequest, accessToken))

    def getPullRequestReview(
        owner: String,
        repo: String,
        pullRequest: Int,
        review: Int,
        accessToken: Option[String] = None): Free[F, GHResponse[PullRequestReview]] =
      Free.inject[PullRequestOp, F](
        GetPullRequestReview(owner, repo, pullRequest, review, accessToken))
  }

  trait Implicits {}

  object implicits extends Implicits
}
