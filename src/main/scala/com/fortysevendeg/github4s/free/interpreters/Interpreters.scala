package com.fortysevendeg.github4s.free.interpreters

import cats.{ApplicativeError, ~>, Eval}
import com.fortysevendeg.github4s.{HttpClient, GithubConfig}
import com.fortysevendeg.github4s.api.{Auth, Repos}
import com.fortysevendeg.github4s.app.{C01, C02, GitHub4s}
import com.fortysevendeg.github4s.free.algebra._
import io.circe.Decoder

trait Interpreters[M[_]] {

  implicit def interpreters(
      implicit
      A: ApplicativeError[M, Throwable],
      C : GithubConfig
  ): GitHub4s ~> M = {
    val repositoryAndUserInterpreter: C01 ~> M = repositoryOpsInterpreter or userOpsInterpreter
    val c01nterpreter: C02 ~> M = requestOpsInterpreter or repositoryAndUserInterpreter
    val all: GitHub4s ~> M = authOpsInterpreter or c01nterpreter
    all
  }

  protected val httpClient = new HttpClient()


  /** Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
    */
  def repositoryOpsInterpreter(implicit A: ApplicativeError[M, Throwable], C : GithubConfig): RepositoryOp ~> M = new (RepositoryOp ~> M) {
    def apply[A](fa: RepositoryOp[A]): M[A] = fa match {
      case GetRepo(owner, repo) ⇒ A.pureEval(Eval.later(Repos.get(owner, repo)))
      case ListCommits(owner, repo, sha, path, author, since, until, pagination) ⇒ A.pureEval(Eval.later(Repos.listCommits(owner, repo, sha, path, author, since, until, pagination)))
    }
  }

  /** Lifts User Ops to an effect capturing Monad such as Task via natural transformations
    */
  def userOpsInterpreter(implicit A: ApplicativeError[M, Throwable], C : GithubConfig): UserOp ~> M = new (UserOp ~> M) {

    import com.fortysevendeg.github4s.api.Users

    def apply[A](fa: UserOp[A]): M[A] = fa match {
      case GetUser(username) ⇒ A.pureEval(Eval.later(Users.get(username)))
      case GetAuthUser() ⇒ A.pureEval(Eval.later(Users.getAuth))
      case GetUsers(since, pagination) ⇒ A.pureEval(Eval.later(Users.getUsers(since, pagination)))
    }
  }

  /** Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
    */
  def authOpsInterpreter(implicit A: ApplicativeError[M, Throwable], C : GithubConfig): AuthOp ~> M = new (AuthOp ~> M) {
    def apply[A](fa: AuthOp[A]): M[A] = fa match {
      case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒ A.pureEval(Eval.later(Auth.newAuth(username, password, scopes, note, client_id, client_secret)))
    }
  }

  /** Lifts Request Ops to an effect capturing Monad such as Task via natural transformations
    */
  def requestOpsInterpreter(implicit App: ApplicativeError[M, Throwable], C : GithubConfig): RequestOp ~> M = new (RequestOp ~> M) {
    def apply[A](fa: RequestOp[A]): M[A] = fa match {
      case Next(url: String, decoder: Decoder[A]) ⇒ {
        //implicit val d: Decoder[A] = decoder
        App.pureEval(Eval.later(httpClient.getByUrl(url, decoder)))
      }
    }
  }


}

