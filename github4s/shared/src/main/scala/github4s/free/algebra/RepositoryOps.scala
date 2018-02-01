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
import github4s.GithubResponses.GHResponse
import github4s.api.Repos
import github4s.free.adt.RepositoryOp._
import github4s.free.domain._
import github4s.free.domain.Repository._

/**
 * Exposes Repositories operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object RepositoryOps {

  @free trait RepositoryOpsM {

    def getRepo(
        owner: String,
        repo: String,
        accessToken: Option[String] = None
    ): FS[GHResponse[Repository]]

    def listOrgRepos(
        org: String,
        `type`: Option[String] = None,
        pagination: Option[Pagination] = None,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[Repository]]]

    def getContents(
        owner: String,
        repo: String,
        path: String,
        ref: Option[String] = None,
        accessToken: Option[String] = None
    ): FS[GHResponse[NonEmptyList[Content]]]

    def listCommits(
        owner: String,
        repo: String,
        sha: Option[String] = None,
        path: Option[String] = None,
        author: Option[String] = None,
        since: Option[String] = None,
        until: Option[String] = None,
        pagination: Option[Pagination] = None,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[Commit]]]

    def listContributors(
        owner: String,
        repo: String,
        anon: Option[String] = None,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[User]]]

    def createRelease(
        owner: String,
        repo: String,
        tagName: String,
        name: String,
        body: String,
        targetCommitish: Option[String] = None,
        draft: Option[Boolean] = None,
        prerelease: Option[Boolean] = None,
        accessToken: Option[String] = None
    ): FS[GHResponse[Release]]

    def getCombinedStatus(
        owner: String,
        repo: String,
        ref: String,
        accessToken: Option[String] = None
    ): FS[GHResponse[CombinedStatus]]

    def listStatuses(
        owner: String,
        repo: String,
        ref: String,
        accessToken: Option[String] = None
    ): FS[GHResponse[List[Status]]]

    def createStatus(
        owner: String,
        repo: String,
        sha: String,
        state: String,
        target_url: Option[String],
        description: Option[String],
        context: Option[String],
        accessToken: Option[String] = None
    ): FS[GHResponse[Status]]
  }

  trait Implicits {

    /**
     * Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
     */
    def repositoryOpsInterpreter: RepositoryOp ~> K = new (RepositoryOp ~> K) {

      val repos = new Repos()

      def apply[A](fa: RepositoryOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case GetRepo(owner, repo, accessToken) ⇒ repos.get(accessToken, headers, owner, repo)
          case ListOrgRepos(org, tipe, pagination, accessToken) ⇒
            repos.listOrgRepos(accessToken, headers, org, tipe, pagination)
          case GetContents(owner, repo, path, ref, accessToken) ⇒
            repos.getContents(accessToken, headers, owner, repo, path, ref)
          case ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken) ⇒
            repos.listCommits(
              accessToken,
              headers,
              owner,
              repo,
              sha,
              path,
              author,
              since,
              until,
              pagination)
          case ListContributors(owner, repo, anon, accessToken) ⇒
            repos.listContributors(accessToken, headers, owner, repo, anon)
          case CreateRelease(
              owner,
              repo,
              tagName,
              name,
              body,
              targetCommitish,
              draft,
              prerelease,
              accessToken) =>
            repos.createRelease(
              accessToken,
              headers,
              owner,
              repo,
              tagName,
              name,
              body,
              targetCommitish,
              draft,
              prerelease)
          case GetCombinedStatus(owner, repo, ref, accessToken) ⇒
            repos.getStatus(accessToken, headers, owner, repo, ref)
          case ListStatuses(owner, repo, ref, accessToken) ⇒
            repos.listStatuses(accessToken, headers, owner, repo, ref)
          case CreateStatus(
              owner,
              repo,
              sha,
              state,
              target_url,
              description,
              context,
              accessToken) ⇒
            repos.createStatus(
              accessToken,
              headers,
              owner,
              repo,
              sha,
              state,
              target_url,
              description,
              context)
        }
      }
    }
  }

  object implicits extends Implicits
}
