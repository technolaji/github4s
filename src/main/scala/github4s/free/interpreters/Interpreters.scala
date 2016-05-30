package github4s.free.interpreters

import cats.{ MonadError, ApplicativeError, ~>, Eval }
import github4s.GithubDefaultUrls._
import github4s.api.{ Users, Auth, Repos }
import github4s.app.{ COGH01, GitHub4s }
import github4s.free.algebra._
import io.circe.Decoder

trait Interpreters {

  implicit def interpreters[M[_]](
    implicit
    A: MonadError[M, Throwable]
  ): GitHub4s ~> M = {
    val c01interpreter: COGH01 ~> M = repositoryOpsInterpreter[M] or userOpsInterpreter[M]
    val all: GitHub4s ~> M = authOpsInterpreter[M] or c01interpreter
    all
  }

  /**
    * Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
    */
  def repositoryOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable]): RepositoryOp ~> M = new (RepositoryOp ~> M) {

    val repos = new Repos()

    def apply[A](fa: RepositoryOp[A]): M[A] = fa match {
      case GetRepo(owner, repo, accessToken) ⇒ A.pureEval(Eval.later(repos.get(accessToken, owner, repo)))
      case ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken) ⇒ A.pureEval(Eval.later(repos.listCommits(accessToken, owner, repo, sha, path, author, since, until, pagination)))
    }
  }

  /**
    * Lifts User Ops to an effect capturing Monad such as Task via natural transformations
    */
  def userOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable]): UserOp ~> M = new (UserOp ~> M) {

    val users = new Users()

    def apply[A](fa: UserOp[A]): M[A] = fa match {
      case GetUser(username, accessToken) ⇒ A.pureEval(Eval.later(users.get(accessToken, username)))
      case GetAuthUser(accessToken) ⇒ A.pureEval(Eval.later(users.getAuth(accessToken)))
      case GetUsers(since, pagination, accessToken) ⇒ A.pureEval(Eval.later(users.getUsers(accessToken, since, pagination)))
    }
  }

  /**
    * Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
    */
  def authOpsInterpreter[M[_]](implicit A: ApplicativeError[M, Throwable]): AuthOp ~> M = new (AuthOp ~> M) {

    val auth = new Auth()

    def apply[A](fa: AuthOp[A]): M[A] = fa match {
      case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒ A.pureEval(Eval.later(auth.newAuth(username, password, scopes, note, client_id, client_secret)))
      case AuthorizeUrl(client_id, redirect_uri, scopes) ⇒ A.pureEval(Eval.later(auth.authorizeUrl(client_id, redirect_uri, scopes)))
      case GetAccessToken(client_id, client_secret, code, redirect_uri, state) ⇒ A.pureEval(Eval.later(auth.getAccessToken(client_id, client_secret, code, redirect_uri, state)))
    }
  }

}

