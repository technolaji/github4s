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
import github4s.free.domain.SearchParam._
import github4s.free.domain.Issue._

/**
 * Issues ops ADT
 */
object IssueOp {

  sealed trait IssueOp[A]

  final case class ListIssues(
      owner: String,
      repo: String,
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[List[Issue]]]

  final case class GetIssue(
      owner: String,
      repo: String,
      number: Int,
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[Issue]]

  final case class SearchIssues(
      query: String,
      searchParams: List[SearchParam],
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[SearchIssuesResult]]

  final case class CreateIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[Issue]]

  final case class EditIssue(
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
  ) extends IssueOp[GHResponse[Issue]]

  final case class ListComments(
      owner: String,
      repo: String,
      number: Int,
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[List[Comment]]]

  final case class CreateComment(
      owner: String,
      repo: String,
      number: Int,
      body: String,
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[Comment]]

  final case class EditComment(
      owner: String,
      repo: String,
      id: Int,
      body: String,
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[Comment]]

  final case class DeleteComment(
      owner: String,
      repo: String,
      id: Int,
      accessToken: Option[String] = None
  ) extends IssueOp[GHResponse[Unit]]

}
