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

import cats.data.NonEmptyList
import github4s.GithubResponses.GHResponse
import github4s.free.domain._

import freestyle._

/**
 * Exposes Repositories operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
@free trait RepositoryOps {

  def getRepo(
      owner: String,
      repo: String,
      accessToken: Option[String]
  ): FS[GHResponse[Repository]]

  def getContents(
      owner: String,
      repo: String,
      path: String,
      ref: Option[String],
      accessToken: Option[String]
  ): FS[GHResponse[NonEmptyList[Content]]]

  def listCommits(
      owner: String,
      repo: String,
      sha: Option[String],
      path: Option[String],
      author: Option[String],
      since: Option[String],
      until: Option[String],
      pagination: Option[Pagination],
      accessToken: Option[String]
  ): FS[GHResponse[List[Commit]]]

  def listContributors(
      owner: String,
      repo: String,
      anon: Option[String],
      accessToken: Option[String]
  ): FS[GHResponse[List[User]]]

  def createRelease(
      owner: String,
      repo: String,
      tagName: String,
      name: String,
      body: String,
      targetCommitish: Option[String],
      draft: Option[Boolean],
      prerelease: Option[Boolean],
      accessToken: Option[String]
  ): FS[GHResponse[Release]]

}
