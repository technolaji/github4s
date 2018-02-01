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

import cats.data.NonEmptyList
import github4s.GithubResponses.GHResponse
import github4s.free.domain._

/**
 * Git ops ADT
 */
sealed trait GitDataOp[A]

final case class GetReference(
    owner: String,
    repo: String,
    ref: String,
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[NonEmptyList[Ref]]]

final case class CreateReference(
    owner: String,
    repo: String,
    ref: String,
    sha: String,
    accessToken: Option[String]
) extends GitDataOp[GHResponse[Ref]]

final case class UpdateReference(
    owner: String,
    repo: String,
    ref: String,
    sha: String,
    force: Boolean,
    accessToken: Option[String]
) extends GitDataOp[GHResponse[Ref]]

final case class GetCommit(
    owner: String,
    repo: String,
    sha: String,
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[RefCommit]]

final case class CreateCommit(
    owner: String,
    repo: String,
    message: String,
    tree: String,
    parents: List[String],
    author: Option[RefAuthor],
    accessToken: Option[String]
) extends GitDataOp[GHResponse[RefCommit]]

final case class CreateBlob(
    owner: String,
    repo: String,
    content: String,
    encoding: Option[String],
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[RefInfo]]

final case class CreateTree(
    owner: String,
    repo: String,
    baseTree: Option[String],
    treeDataList: List[TreeData],
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[TreeResult]]

final case class CreateTag(
    owner: String,
    repo: String,
    tag: String,
    message: String,
    objectSha: String,
    objectType: String,
    author: Option[RefAuthor],
    accessToken: Option[String]
) extends GitDataOp[GHResponse[Tag]]
