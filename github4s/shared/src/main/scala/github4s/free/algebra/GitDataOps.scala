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

import cats.data.NonEmptyList
import cats.~>
import freestyle.free._
import github4s.GithubResponses._
import github4s.api.GitData
import github4s.free.adt.GitDataOp._
import github4s.free.domain.GitData._

/**
 * Exposes Git Data operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object GitDataOps {

  @free trait GitDataOpsM {

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
        force: Boolean,
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

  trait Implicits {

    /**
     * Lifts Git Ops to an effect capturing Monad such as Task via natural transformations
     */
    def gitDataOpsInterpreter: GitDataOp ~> K =
      new (GitDataOp ~> K) {

        val gitData = new GitData()

        def apply[A](fa: GitDataOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case GetReference(owner, repo, ref, accessToken) ⇒
              gitData.reference(accessToken, headers, owner, repo, ref)
            case CreateReference(owner, repo, ref, sha, accessToken) ⇒
              gitData.createReference(accessToken, headers, owner, repo, ref, sha)
            case UpdateReference(owner, repo, ref, sha, force, accessToken) ⇒
              gitData.updateReference(accessToken, headers, owner, repo, ref, sha, force)
            case GetCommit(owner, repo, sha, accessToken) ⇒
              gitData.commit(accessToken, headers, owner, repo, sha)
            case CreateCommit(owner, repo, message, tree, parents, author, accessToken) ⇒
              gitData
                .createCommit(accessToken, headers, owner, repo, message, tree, parents, author)
            case CreateBlob(owner, repo, content, encoding, accessToken) ⇒
              gitData.createBlob(accessToken, headers, owner, repo, content, encoding)
            case CreateTree(owner, repo, baseTree, treeDataList, accessToken) ⇒
              gitData.createTree(accessToken, headers, owner, repo, baseTree, treeDataList)
            case CreateTag(owner, repo, tag, message, objectSha, objectType, author, accessToken) ⇒
              gitData.createTag(
                accessToken,
                headers,
                owner,
                repo,
                tag,
                message,
                objectSha,
                objectType,
                author)
          }
        }
      }

  }

  object implicits extends Implicits
}
