package github4s.free.interpreters

import cats.implicits._
import cats.{ ApplicativeError, Eval, MonadError, ~> }
import github4s.GithubDefaultUrls._
import github4s.api.{ Auth, Gists, Repos, Users }
import github4s.app.{ COGH01, GitHub4s }
import github4s.free.algebra._
import io.circe.Decoder
import simulacrum.typeclass

@typeclass trait Capture[M[_]] {
  def capture[A](a: ⇒ A): M[A]
}

trait Interpreters {

  implicit def interpreters[M[_]](
    implicit
    A: MonadError[M, Throwable],
    C: Capture[M]
  ): GitHub4s ~> M = {
    val c01interpreter: COGH01 ~> M = repositoryOpsInterpreter[M] or userOpsInterpreter[M]
    val all: GitHub4s ~> M = authOpsInterpreter[M] or c01interpreter
    all
  }

  /**
    * Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
    */
  def repositoryOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable], C: Capture[M]): RepositoryOp ~> M = new (RepositoryOp ~> M) {

    val repos = new Repos()

    def apply[A](fa: RepositoryOp[A]): M[A] = fa match {
      case GetRepo(owner, repo, accessToken) ⇒ C.capture(repos.get(accessToken, owner, repo))
      case ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken) ⇒ C.capture(repos.listCommits(accessToken, owner, repo, sha, path, author, since, until, pagination))
      case ListContributors(owner, repo, anon, accessToken) ⇒ C.capture(repos.listContributors(accessToken, owner, repo, anon))
    }
  }

  /**
    * Lifts User Ops to an effect capturing Monad such as Task via natural transformations
    */
  def userOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable], C: Capture[M]): UserOp ~> M = new (UserOp ~> M) {

    val users = new Users()

    def apply[A](fa: UserOp[A]): M[A] = fa match {
      case GetUser(username, accessToken) ⇒ C.capture(users.get(accessToken, username))
      case GetAuthUser(accessToken) ⇒ C.capture(users.getAuth(accessToken))
      case GetUsers(since, pagination, accessToken) ⇒ C.capture(users.getUsers(accessToken, since, pagination))
    }
  }

  /**
    * Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
    */
  def authOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable], C: Capture[M]): AuthOp ~> M = new (AuthOp ~> M) {

    val auth = new Auth()

    def apply[A](fa: AuthOp[A]): M[A] = fa match {
      case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒ C.capture(auth.newAuth(username, password, scopes, note, client_id, client_secret))
      case AuthorizeUrl(client_id, redirect_uri, scopes) ⇒ C.capture(auth.authorizeUrl(client_id, redirect_uri, scopes))
      case GetAccessToken(client_id, client_secret, code, redirect_uri, state) ⇒ C.capture(auth.getAccessToken(client_id, client_secret, code, redirect_uri, state))
    }
  }

  /**
    * Lifts Gist Ops to an effect capturing Monad such as Task via natural transformations
    */
  def gistOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable], C: Capture[M]): GistOp ~> M = new (GistOp ~> M) {

    val gists = new Gists()

    def apply[A](fa: GistOp[A]): M[A] = fa match {
      case NewGist(description, public, files, accessToken) ⇒ C.capture(gists.newGist(description, public, files, accessToken))
    }
  }

}
