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
import cats.free.{Free, Inject}
import github4s.GithubResponses._
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

final case class UpdateReference(
  owner: String,
  repo: String,
  ref: String,
  sha: String,
  force: Option[Boolean],
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
    author: Option[RefCommitAuthor],
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

/**
 * Exposes Git Data operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class GitDataOps[F[_]](implicit I: Inject[GitDataOp, F]) {

  def getReference(
      owner: String,
      repo: String,
      ref: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[NonEmptyList[Ref]]] =
    Free.inject[GitDataOp, F](GetReference(owner, repo, ref, accessToken))

  def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
    force: Option[Boolean],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Ref]] =
    Free.inject[GitDataOp, F](UpdateReference(owner, repo, ref, sha, force, accessToken))

  def getCommit(
      owner: String,
      repo: String,
      sha: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[RefCommit]] =
    Free.inject[GitDataOp, F](GetCommit(owner, repo, sha, accessToken))

  def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String],
      author: Option[RefCommitAuthor],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[RefCommit]] =
    Free.inject[GitDataOp, F](CreateCommit(owner, repo, message, tree, parents, author, accessToken))

  def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[RefInfo]] =
    Free.inject[GitDataOp, F](CreateBlob(owner, repo, content, encoding, accessToken))

  def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[TreeResult]] =
    Free.inject[GitDataOp, F](CreateTree(owner, repo, baseTree, treeDataList, accessToken))
}

/**
 * Default implicit based DI factory from which instances of the GitOps may be obtained
 */
object GitDataOps {

  implicit def instance[F[_]](implicit I: Inject[GitDataOp, F]): GitDataOps[F] = new GitDataOps[F]

}
