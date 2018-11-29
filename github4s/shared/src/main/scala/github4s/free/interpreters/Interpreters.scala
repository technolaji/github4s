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

package github4s.free.interpreters

import cats.data.Kleisli
import cats.{~>, ApplicativeError, MonadError}
import github4s.GithubDefaultUrls._
import github4s.HttpRequestBuilderExtension
import github4s.api._
import github4s.app._
import github4s.free.algebra._
import simulacrum.typeclass

import scala.language.higherKinds

@typeclass
trait Capture[M[_]] {
  def capture[A](a: ⇒ A): M[A]
}

class Interpreters[M[_], C](
                             implicit A: ApplicativeError[M, Throwable],
                             C: Capture[M],
                             httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  type K[A] = Kleisli[M, Map[String, String], A]

  implicit def interpreters(
                             implicit A: MonadError[M, Throwable]
                           ): GitHub4s ~> K = {
    val c01interpreter: COGH01 ~> K = repositoryOpsInterpreter or userOpsInterpreter
    val c02interpreter: COGH02 ~> K = gistOpsInterpreter or c01interpreter
    val c03interpreter: COGH03 ~> K = issueOpsInterpreter or c02interpreter
    val c04interpreter: COGH04 ~> K = authOpsInterpreter or c03interpreter
    val c05interpreter: COGH05 ~> K = gitDataOpsInterpreter or c04interpreter
    val c06interpreter: COGH06 ~> K = pullRequestOpsInterpreter or c05interpreter
    val c07interpreter: COGH07 ~> K = activityOpsInterpreter or c06interpreter
    val c08interpreter: COGH08 ~> K = webhookOpsInterpreter or c07interpreter
    val all: GitHub4s ~> K = organizationOpsInterpreter or c08interpreter
    all
  }

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
        case ListUserRepos(user, tipe, pagination, accessToken) ⇒
          repos.listUserRepos(accessToken, headers, user, tipe, pagination)
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
        case ListCollaborators(owner, repo, affiliation, accessToken) =>
          repos.listCollaborators(accessToken, headers, owner, repo, affiliation)
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
        case CreateStatus(owner, repo, sha, state, target_url, description, context, accessToken) ⇒
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

  /**
    * Lifts User Ops to an effect capturing Monad such as Task via natural transformations
    */
  def userOpsInterpreter: UserOp ~> K =
    new (UserOp ~> K) {

      val users = new Users()

      def apply[A](fa: UserOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case GetUser(username, accessToken) ⇒ users.get(accessToken, headers, username)
          case GetAuthUser(accessToken) ⇒ users.getAuth(accessToken, headers)
          case GetUsers(since, pagination, accessToken) ⇒
            users.getUsers(accessToken, headers, since, pagination)
        }
      }
    }

  /**
    * Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
    */
  def authOpsInterpreter: AuthOp ~> K =
    new (AuthOp ~> K) {

      val auth = new Auth()

      def apply[A](fa: AuthOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒
            auth.newAuth(username, password, scopes, note, client_id, client_secret, headers)
          case AuthorizeUrl(client_id, redirect_uri, scopes) ⇒
            auth.authorizeUrl(client_id, redirect_uri, scopes)
          case GetAccessToken(client_id, client_secret, code, redirect_uri, state) ⇒
            auth.getAccessToken(client_id, client_secret, code, redirect_uri, state, headers)
        }
      }
    }

  /**
    * Lifts Gist Ops to an effect capturing Monad such as Task via natural transformations
    */
  def gistOpsInterpreter: GistOp ~> K =
    new (GistOp ~> K) {

      val gists = new Gists()

      def apply[A](fa: GistOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case NewGist(description, public, files, accessToken) ⇒
            gists.newGist(description, public, files, headers, accessToken)
        }
      }
    }

  /**
    * Lifts Issue Ops to an effect capturing Monad such as Task via natural transformations
    */
  def issueOpsInterpreter: IssueOp ~> K =
    new (IssueOp ~> K) {

      val issues = new Issues()

      def apply[A](fa: IssueOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case ListIssues(owner, repo, accessToken) ⇒
            issues.list(accessToken, headers, owner, repo)
          case GetIssue(owner, repo, number, accessToken) ⇒
            issues.get(accessToken, headers, owner, repo, number)
          case SearchIssues(query, searchParams, accessToken) ⇒
            issues.search(accessToken, headers, query, searchParams)
          case CreateIssue(owner, repo, title, body, milestone, labels, assignees, accessToken) ⇒
            issues
              .create(accessToken, headers, owner, repo, title, body, milestone, labels, assignees)
          case EditIssue(
          owner,
          repo,
          issue,
          state,
          title,
          body,
          milestone,
          labels,
          assignees,
          accessToken) ⇒
            issues.edit(
              accessToken,
              headers,
              owner,
              repo,
              issue,
              state,
              title,
              body,
              milestone,
              labels,
              assignees)
          case ListComments(owner, repo, number, accessToken) ⇒
            issues.listComments(accessToken, headers, owner, repo, number)
          case CreateComment(owner, repo, number, body, accessToken) ⇒
            issues.createComment(accessToken, headers, owner, repo, number, body)
          case EditComment(owner, repo, id, body, accessToken) ⇒
            issues.editComment(accessToken, headers, owner, repo, id, body)
          case DeleteComment(owner, repo, id, accessToken) ⇒
            issues.deleteComment(accessToken, headers, owner, repo, id)
          case ListLabels(owner, repo, number, accessToken) ⇒
            issues.listLabels(accessToken, headers, owner, repo, number)
          case AddLabels(owner, repo, number, labels, accessToken) ⇒
            issues.addLabels(accessToken, headers, owner, repo, number, labels)
          case RemoveLabel(owner, repo, number, label, accessToken) ⇒
            issues.removeLabel(accessToken, headers, owner, repo, number, label)
          case ListAvailableAssignees(owner, repo, pagination, accessToken) ⇒
            issues.listAvailableAssignees(accessToken, headers, owner, repo, pagination)
        }
      }
    }

  /**
    * Lifts Activity Ops to an effect capturing Monad such as Task via natural transformations
    */
  def activityOpsInterpreter: ActivityOp ~> K =
    new (ActivityOp ~> K) {

      val activities = new Activities()

      def apply[A](fa: ActivityOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case SetThreadSub(id, subscribed, ignored, accessToken) ⇒
            activities.setThreadSub(accessToken, headers, id, subscribed, ignored)
          case ListStargazers(owner, repo, timeline, pagination, accessToken) ⇒
            activities.listStargazers(accessToken, headers, owner, repo, timeline, pagination)
          case ListStarredRepositories(
          username, timeline, sort, direction, pagination, accessToken) ⇒
            activities.listStarredRepositories(
              accessToken, headers, username, timeline, sort, direction, pagination)
        }
      }
    }

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
            gitData.createCommit(accessToken, headers, owner, repo, message, tree, parents, author)
          case CreateBlob(owner, repo, content, encoding, accessToken) ⇒
            gitData.createBlob(accessToken, headers, owner, repo, content, encoding)
          case CreateTree(owner, repo, baseTree, treeDataList, accessToken) ⇒
            gitData.createTree(accessToken, headers, owner, repo, baseTree, treeDataList)
          case GetTree(owner, repo, sha, recursive, accessToken) ⇒
            gitData.tree(accessToken, headers, owner, repo, sha, recursive)
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

  /**
    * Lifts PullRequest Ops to an effect capturing Monad such as Task via natural transformations
    */
  def pullRequestOpsInterpreter: PullRequestOp ~> K =
    new (PullRequestOp ~> K) {

      val pullRequests = new PullRequests()

      def apply[A](fa: PullRequestOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case GetPullRequest(owner, repo, number, accessToken) ⇒
            pullRequests.get(accessToken, headers, owner, repo, number)
          case ListPullRequests(owner, repo, filters, accessToken, pagination) ⇒
            pullRequests.list(accessToken, headers, owner, repo, filters, pagination)
          case ListPullRequestFiles(owner, repo, number, accessToken, pagination) ⇒
            pullRequests.listFiles(accessToken, headers, owner, repo, number, pagination)
          case CreatePullRequest(
          owner,
          repo,
          newPullRequest,
          head,
          base,
          maintainerCanModify,
          accessToken) ⇒
            pullRequests
              .create(
                accessToken,
                headers,
                owner,
                repo,
                newPullRequest,
                head,
                base,
                maintainerCanModify)
          case ListPullRequestReviews(owner, repo, pullRequest, accessToken, pagination) ⇒
            pullRequests.listReviews(accessToken, headers, owner, repo, pullRequest, pagination)
          case GetPullRequestReview(owner, repo, pullRequest, review, accessToken) ⇒
            pullRequests.getReview(accessToken, headers, owner, repo, pullRequest, review)
        }
      }
    }

  /**
    * Lifts Organization Ops to an effect capturing Monad such as Task via natural transformations
    */
  def organizationOpsInterpreter: OrganizationOp ~> K =
    new (OrganizationOp ~> K) {

      val organizations = new Organizations()

      def apply[A](fa: OrganizationOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case ListMembers(org, filter, role, pagination, accessToken) ⇒
            organizations.listMembers(accessToken, headers, org, filter, role, pagination)
          case ListOutsideCollaborators(org, filter, pagination, accessToken) ⇒
            organizations.listOutsideCollaborators(accessToken, headers, org, filter, pagination)
        }
      }
    }


  /**
    * Lifts Webhook Ops to an effect capturing Monad such as Task via natural transformations
    */
  def webhookOpsInterpreter: WebhookOp ~> K =
    new (WebhookOp ~> K) {

      val webhooks = new Webhooks()

      def apply[A](fa: WebhookOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case ListWebhooks(accessToken, owner, repo) ⇒
            webhooks.listWebhooks(accessToken, headers, owner, repo)
        }
      }
    }


}
