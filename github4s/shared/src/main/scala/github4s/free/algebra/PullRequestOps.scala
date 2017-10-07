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

package github4s.free.algebra

import cats.InjectK
import cats.free.Free
import github4s.GithubResponses._
import github4s.free.domain._

/**
 * Exposes Pull Request operations as a Free monadic algebra that may be combined with other
 * Algebras via Coproduct
 */
@free trait PullRequestOps {

  def listPullRequests(
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil
  ): FS[GHResponse[List[PullRequest]]]

  def listPullRequestFiles(
      owner: String,
      repo: String,
      number: Int
  ): FS[GHResponse[List[PullRequestFile]]]

  def createPullRequest(
      owner: String,
      repo: String,
      newPullRequest: NewPullRequest,
      head: String,
      base: String,
      maintainerCanModify: Option[Boolean] = Some(true)
  ): FS[GHResponse[PullRequest]]

  def listPullRequestReviews(
      owner: String,
      repo: String,
      pullRequest: Int
  ): FS[GHResponse[List[PullRequestReview]]]

  def getPullRequestReview(
      owner: String,
      repo: String,
      pullRequest: Int,
      review: Int
  ): FS[GHResponse[PullRequestReview]]
}
