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
import github4s.GithubResponses._
import github4s.free.domain._

import freestyle._

/**
 * Exposes Git Data operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
@free trait GitDataOps {

  def getReference(
      owner: String,
      repo: String,
      ref: String,
      accessToken: Option[String] = None
  ): FS[GHResponse[NonEmptyList[Ref]]]

  def createReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      accessToken: Option[String] = None
  ): FS[GHResponse[Ref]]

  def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      force: Option[Boolean],
      accessToken: Option[String] = None
  ): FS[GHResponse[Ref]]

  def getCommit(
      owner: String,
      repo: String,
      sha: String,
      accessToken: Option[String] = None
  ): FS[GHResponse[RefCommit]]

  def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String],
      author: Option[RefAuthor],
      accessToken: Option[String] = None
  ): FS[GHResponse[RefCommit]]

  def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String],
      accessToken: Option[String] = None
  ): FS[GHResponse[RefInfo]]

  def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData],
      accessToken: Option[String] = None
  ): FS[GHResponse[TreeResult]]

  def createTag(
      owner: String,
      repo: String,
      tag: String,
      message: String,
      objectSha: String,
      objectType: String,
      author: Option[RefAuthor],
      accessToken: Option[String] = None
  ): FS[GHResponse[Tag]]
}
