package com.fortysevendeg.github4s.free.interpreters

import cats.{ MonadError, ApplicativeError, ~>, Eval }
import com.fortysevendeg.github4s.HttpClient
import com.fortysevendeg.github4s.api.{ Auth, Repos }
import com.fortysevendeg.github4s.app.{ COGH01, GitHub4s }
import com.fortysevendeg.github4s.free.algebra._
import io.circe.Decoder

trait Interpreters[M[_]] {

  implicit def interpreters(
    implicit
    A: MonadError[M, Throwable]
  ): GitHub4s ~> M = {
    val c01interpreter: COGH01 ~> M = repositoryOpsInterpreter or userOpsInterpreter
    val all: GitHub4s ~> M = authOpsInterpreter or c01interpreter
    all
  }

  protected val httpClient = new HttpClient()

  /**
    * Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
    */
  def repositoryOpsInterpreter(implicit A: ApplicativeError[M, Throwable]): RepositoryOp ~> M = new (RepositoryOp ~> M) {
    def apply[A](fa: RepositoryOp[A]): M[A] = fa match {
      case GetRepo(owner, repo, accessToken) ⇒ A.pureEval(Eval.later(Repos.get(accessToken, owner, repo)))
      case ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken) ⇒ A.pureEval(Eval.later(Repos.listCommits(accessToken, owner, repo, sha, path, author, since, until, pagination)))
    }
  }

  /**
    * Lifts User Ops to an effect capturing Monad such as Task via natural transformations
    */
  def userOpsInterpreter(implicit A: ApplicativeError[M, Throwable]): UserOp ~> M = new (UserOp ~> M) {

    import com.fortysevendeg.github4s.api.Users

    def apply[A](fa: UserOp[A]): M[A] = fa match {
      case GetUser(username, accessToken) ⇒ A.pureEval(Eval.later(Users.get(accessToken, username)))
      case GetAuthUser(accessToken) ⇒ A.pureEval(Eval.later(Users.getAuth(accessToken)))
      case GetUsers(since, pagination, accessToken) ⇒ A.pureEval(Eval.later(Users.getUsers(accessToken, since, pagination)))
    }
  }

  /**
    * Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
    */
  def authOpsInterpreter(implicit A: ApplicativeError[M, Throwable]): AuthOp ~> M = new (AuthOp ~> M) {
    def apply[A](fa: AuthOp[A]): M[A] = fa match {
      case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒ A.pureEval(Eval.later(Auth.newAuth(username, password, scopes, note, client_id, client_secret)))
      case AuthorizeUrl(client_id, redirect_uri, scopes) ⇒ A.pureEval(Eval.later(Auth.authorizeUrl(client_id, redirect_uri, scopes)))
      case GetAccessToken(client_id, client_secret, code, redirect_uri, state) ⇒ A.pureEval(Eval.later(Auth.getAccessToken(client_id, client_secret, code, redirect_uri, state)))
    }
  }

}

