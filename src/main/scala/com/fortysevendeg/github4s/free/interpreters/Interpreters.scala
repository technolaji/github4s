package com.fortysevendeg.github4s.free.interpreters

import cats.{ApplicativeError, ~>, Eval}
import com.fortysevendeg.github4s.app.{C01, GitHub4s}
import com.fortysevendeg.github4s.free.algebra._

trait Interpreters[M[_]] {

  implicit def interpreters(
      implicit
      A: ApplicativeError[M, Throwable]
  ): GitHub4s ~> M = {
    val repositoryAndUserInterpreter: C01 ~> M = repositoryOpsInterpreter or userOpsInterpreter
    val all: GitHub4s ~> M = gistOpsInterpreter or repositoryAndUserInterpreter
    all
  }


  /** Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
    */
  implicit def repositoryOpsInterpreter(implicit A: ApplicativeError[M, Throwable]): RepositoryOp ~> M = new (RepositoryOp ~> M) {
    def apply[A](fa: RepositoryOp[A]): M[A] = fa match {
      case ListYourRepos() ⇒ A.pureEval(Eval.defer(???))
      case ListUserRepos() ⇒ A.pureEval(Eval.defer(???))
      case ListOrganizationRepos() ⇒ A.pureEval(Eval.defer(???))
    }
  }

  /** Lifts User Ops to an effect capturing Monad such as Task via natural transformations
    */
  implicit def userOpsInterpreter(implicit A: ApplicativeError[M, Throwable]): UserOp ~> M = new (UserOp ~> M) {

    import com.fortysevendeg.github4s.api.Users

    def apply[A](fa: UserOp[A]): M[A] = fa match {
      case GetUser(username) ⇒ A.pureEval(Eval.later(Users.get(username)))
      case GetAuthUser() ⇒ A.pureEval(Eval.later(???))
      case GetUsers() ⇒ A.pureEval(Eval.later(???))
    }
  }

  /** Lifts Gist Ops to an effect capturing Monad such as Task via natural transformations
    */
  implicit def gistOpsInterpreter(implicit A: ApplicativeError[M, Throwable]): GistOp ~> M = new (GistOp ~> M) {
    def apply[A](fa: GistOp[A]): M[A] = fa match {
      case GetGists() ⇒ A.pureEval(Eval.defer(???))
      case GetAuthGists() ⇒ A.pureEval(Eval.defer(???))
    }
  }


}

