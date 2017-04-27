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

import github4s.GithubResponses._
import github4s.free.domain.{Issue, SearchIssuesResult, SearchParam}

import freestyle._

/**
 * Exposes Issue operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
@free trait IssueOps {

  def listIssues(
      owner: String,
      repo: String,
  ): FS[GHResponse[List[Issue]]]

  def searchIssues(
      query: String,
      searchParams: List[SearchParam]
  ): FS[GHResponse[SearchIssuesResult]]

  def createIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String]
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
      assignees: List[String]
  ): FS[GHResponse[Issue]]
}
