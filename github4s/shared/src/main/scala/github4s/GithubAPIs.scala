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

package github4s

import cats.data.NonEmptyList
import github4s.GithubResponses.GHResponse
import github4s.free.algebra._
import github4s.free.domain._
import freestyle._

class GHUsers[F[_]](implicit O: UserOps[F]) {

  def get(username: String): FreeS[F, GHResponse[User]] = O.getUser(username )

  def getAuth: FreeS[F, GHResponse[User]] = O.getAuthUser()

  def getUsers(
      since: Int,
      pagination: Option[Pagination] = None): FreeS[F, GHResponse[List[User]]] =
    O.getUsers(since, pagination)

}

class GHRepos[F[_]](implicit O: RepositoryOps[F]) {

  def get(owner: String, repo: String): FreeS[F, GHResponse[Repository]] =
    O.getRepo(owner, repo)

  def listOrgRepos(
      org: String,
      `type`: Option[String] = None,
      pagination: Option[Pagination] = None
  ): GHIO[GHResponse[List[Repository]]] =
    O.listOrgRepos(org, `type`, pagination, accessToken)

  def getContents(
      owner: String,
      repo: String,
      path: String,
      ref: Option[String] = None): FreeS[F, GHResponse[NonEmptyList[Content]]] =
    O.getContents(owner, repo, path, ref)

  def listCommits(
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = None
  ): FreeS[F, GHResponse[List[Commit]]] =
    O.listCommits(owner, repo, sha, path, author, since, until, pagination)

  def listContributors(
      owner: String,
      repo: String,
      anon: Option[String] = None
  ): FreeS[F, GHResponse[List[User]]] =
    O.listContributors(owner, repo, anon)

  def createRelease(
      owner: String,
      repo: String,
      tagName: String,
      name: String,
      body: String,
      targetCommitish: Option[String] = None,
      draft: Option[Boolean] = None,
      prerelease: Option[Boolean] = None
  ): FreeS[F, GHResponse[Release]] =
    O.createRelease(
      owner,
      repo,
      tagName,
      name,
      body,
      targetCommitish,
      draft,
      prerelease,
      )

  def getCombinedStatus(
      owner: String,
      repo: String,
      ref: String
  ): GHIO[GHResponse[CombinedStatus]] =
    O.getCombinedStatus(owner, repo, ref, accessToken)

  def listStatuses(
      owner: String,
      repo: String,
      ref: String
  ): GHIO[GHResponse[List[Status]]] =
    O.listStatuses(owner, repo, ref, accessToken)

  def createStatus(
      owner: String,
      repo: String,
      sha: String,
      state: String,
      target_url: Option[String] = None,
      description: Option[String] = None,
      context: Option[String] = None
  ): GHIO[GHResponse[Status]] =
    O.createStatus(owner, repo, sha, state, target_url, description, context, accessToken)
}

class GHAuth[F[_]](implicit O: AuthOps[F]) {

  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String
  ): FreeS[F, GHResponse[Authorization]] =
    O.newAuth(username, password, scopes, note, client_id, client_secret)

  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]
  ): FreeS[F, GHResponse[Authorize]] =
    O.authorizeUrl(client_id, redirect_uri, scopes)

  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String
  ): FreeS[F, GHResponse[OAuthToken]] =
    O.getAccessToken(client_id, client_secret, code, redirect_uri, state)
}

class GHGists[F[_]](implicit O: GistOps[F]) {
  def newGist(
      description: String,
      public: Boolean,
      files: Map[String, GistFile]
  ): FreeS[F, GHResponse[Gist]] =
    O.newGist(description, public, files)
}

class GHIssues[F[_]](implicit O: IssueOps[F]) {

  def listIssues(
      owner: String,
      repo: String
  ): FreeS[F, GHResponse[List[Issue]]] =
    O.listIssues(owner, repo)

  def searchIssues(
      query: String,
      searchParams: List[SearchParam]
  ): FreeS[F, GHResponse[SearchIssuesResult]] =
    O.searchIssues(query, searchParams)

  def createIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int] = None,
      labels: List[String] = List.empty,
      assignees: List[String] = List.empty
  ): FreeS[F, GHResponse[Issue]] =
    O.createIssue(owner, repo, title, body, milestone, labels, assignees)

  def editIssue(
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int] = None,
      labels: List[String] = List.empty,
      assignees: List[String] = List.empty
  ): FreeS[F, GHResponse[Issue]] =
    O.editIssue(owner, repo, issue, state, title, body, milestone, labels, assignees)

  def createComment(
      owner: String,
      repo: String,
      number: Int,
      body: String
  ): GHIO[GHResponse[Comment]] =
    O.createComment(owner, repo, number, body, accessToken)
  def editComment(
      owner: String,
      repo: String,
      id: Int,
      body: String
  ): GHIO[GHResponse[Comment]] =
    O.editComment(owner, repo, id, body, accessToken)

  def deleteComment(
      owner: String,
      repo: String,
      id: Int
  ): GHIO[GHResponse[Unit]] =
    O.deleteComment(owner, repo, id, accessToken)

}

class GHActivities(accessToken: Option[String] = None)(implicit O: ActivityOps[GitHub4s]) {

  def setThreadSub(
      id: Int,
      subscribed: Boolean,
      ignored: Boolean): GHIO[GHResponse[Subscription]] =
    O.setThreadSub(id, subscribed, ignored, accessToken)

  def listStargazers(
      owner: String,
      repo: String,
      timeline: Boolean,
      pagination: Option[Pagination] = None
  ): GHIO[GHResponse[List[Stargazer]]] =
    O.listStargazers(owner, repo, timeline, pagination, accessToken)

  def listStarredRepositories(
      username: String,
      timeline: Boolean,
      sort: Option[String] = None,
      direction: Option[String] = None,
      pagination: Option[Pagination] = None
  ): GHIO[GHResponse[List[StarredRepository]]] =
    O.listStarredRepositories(username, timeline, sort, direction, pagination, accessToken)
}

class GHGitData[F[_]](implicit O: GitDataOps[F]) {

  def getReference(
      owner: String,
      repo: String,
      ref: String
  ): FreeS[F, GHResponse[NonEmptyList[Ref]]] =
    O.getReference(owner, repo, ref)

  def createReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String
  ): FreeS[F, GHResponse[Ref]] =
    O.createReference(owner, repo, ref, sha)

  def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      force: Boolean = false
  ): FreeS[F, GHResponse[Ref]] =
    O.updateReference(owner, repo, ref, sha, force)

  def getCommit(
      owner: String,
      repo: String,
      sha: String
  ): FreeS[F, GHResponse[RefCommit]] =
    O.getCommit(owner, repo, sha)

  def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String] = Nil,
      author: Option[RefAuthor] = None
  ): FreeS[F, GHResponse[RefCommit]] =
    O.createCommit(owner, repo, message, tree, parents, author)

  def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String]
  ): FreeS[F, GHResponse[RefInfo]] =
    O.createBlob(owner, repo, content, encoding)

  def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData]
  ): FreeS[F, GHResponse[TreeResult]] =
    O.createTree(owner, repo, baseTree, treeDataList)

  def createTag(
      owner: String,
      repo: String,
      tag: String,
      message: String,
      objectSha: String,
      objectType: String,
      author: Option[RefAuthor] = None
  ): FreeS[F, GHResponse[Tag]] =
    O.createTag(owner, repo, tag, message, objectSha, objectType, author)
}

class GHPullRequests[F[_]](implicit O: PullRequestOps[F]) {
  def list(
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil
  ): FreeS[F, GHResponse[List[PullRequest]]] =
    O.listPullRequests(owner, repo, filters)

  def listFiles(
      owner: String,
      repo: String,
      number: Int
  ): FreeS[F, GHResponse[List[PullRequestFile]]] =
    O.listPullRequestFiles(owner, repo, number)
}

class GHStatuses[F[_]](implicit O: StatusOps[F]) {
  def getCombinedStatus(
      owner: String,
      repo: String,
      ref: String
  ): FreeS[F, GHResponse[CombinedStatus]] =
    O.getCombinedStatus(owner, repo, ref)

  def listReviews(
      owner: String,
      repo: String,
      ref: String
  ): FreeS[F, GHResponse[List[Status]]] =
    O.listStatuses(owner, repo, ref)

  def getReview(
      owner: String,
      repo: String,
      sha: String,
      state: String,
      target_url: Option[String] = None,
      description: Option[String] = None,
      context: Option[String] = None
  ): FreeS[F, GHResponse[Status]] =
    O.createStatus(owner, repo, sha, state, target_url, description, context)
}

class GHOrganizations(accessToken: Option[String] = None)(implicit O: OrganizationOps[GitHub4s]) {

  def listMembers(
      org: String,
      filter: Option[String] = None,
      role: Option[String] = None,
      pagination: Option[Pagination] = None
  ): GHIO[GHResponse[List[User]]] =
    O.listMembers(org, filter, role, pagination, accessToken)
}