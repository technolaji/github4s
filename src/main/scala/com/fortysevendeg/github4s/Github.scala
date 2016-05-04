package com.fortysevendeg.github4s

import cats.data.{OptionT, XorT}
import cats.{MonadError, ~>}
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.app._


case class GithubConfig(accessToken : Option[String] = None)


class Github(accessToken : Option[String] = None) {

  lazy val users = new GHUsers(accessToken)
  lazy val repos = new GHRepos(accessToken)
  lazy val auth = new GHAuth(accessToken)

}

object Github {

  def apply(accessToken: Option[String] = None) = new Github(accessToken)

  implicit class GithubIOSyntaxXOR[A](gio : GHIO[GHResponse[A]]) {

    def exec[M[_]](implicit I : (GitHub4s ~> M), A: MonadError[M, Throwable]): M[GHResponse[A]] = gio foldMap I

    def liftGH: XorT[GHIO, GHException, GHResult[A]] = XorT[GHIO, GHException, GHResult[A]](gio)

  }
}