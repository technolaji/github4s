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

package github4s.free.adt

import github4s.GithubResponses.GHResponse
import github4s.free.domain.PullRequest._

/**
 * PullRequests ops ADT
 */
object PullRequestOp {

  sealed trait PullRequestOp[A]

  final case class ListPullRequests(
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil,
      accessToken: Option[String] = None
  ) extends PullRequestOp[GHResponse[List[PullRequest]]]

  final case class ListPullRequestFiles(
      owner: String,
      repo: String,
      number: Int,
      accessToken: Option[String] = None
  ) extends PullRequestOp[GHResponse[List[PullRequestFile]]]

  final case class CreatePullRequest(
      owner: String,
      repo: String,
      newPullRequest: NewPullRequest,
      head: String,
      base: String,
      maintainerCanModify: Option[Boolean] = Some(true),
      accessToken: Option[String] = None
  ) extends PullRequestOp[GHResponse[PullRequest]]

  final case class ListPullRequestReviews(
      owner: String,
      repo: String,
      pullRequest: Int,
      accessToken: Option[String] = None
  ) extends PullRequestOp[GHResponse[List[PullRequestReview]]]

  final case class GetPullRequestReview(
      owner: String,
      repo: String,
      pullRequest: Int,
      review: Int,
      accessToken: Option[String] = None
  ) extends PullRequestOp[GHResponse[PullRequestReview]]

}
