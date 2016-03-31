package com.fortysevendeg.github4s

import cats.free.Free
import cats.{ApplicativeError, Monad, ~>}
import com.fortysevendeg.github4s.Github.GithubIO
import com.fortysevendeg.github4s.app._
import com.fortysevendeg.github4s.free.algebra.UserOps
import com.fortysevendeg.github4s.free.domain.Collaborator

class Github(implicit U : UserOps[GitHub4s]) {

  def getUser(username : String): GithubIO[Option[Collaborator]] = U.getUser(username)

}

object Github {

  type GithubIO[A] = Free[GitHub4s, A]

  def apply() = new Github

  implicit class GithubIOSyntax[A](gio : Free[GitHub4s, A]) {

    def exec[M[_]](implicit M: Monad[M], I : (GitHub4s ~> M), A: ApplicativeError[M, Throwable]) = gio foldMap I

  }

}