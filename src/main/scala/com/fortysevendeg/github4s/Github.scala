package com.fortysevendeg.github4s

import cats.data.{OptionT, XorT}
import cats.{ApplicativeError, Monad, ~>}
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.app._


case class GithubConfig(accessToken : Option[String] = None, extraHeaders : Map[String, String] = Map.empty:Map[String, String])


object Github {

  lazy val users = new GHUsers()
  lazy val repos = new GHRepos()
  lazy val auth = new GHAuth()


  implicit class GithubIOSyntaxXOR[A](gio : GHIO[GHResponse[A]]) {

    def exec[M[_]](implicit C : GithubConfig, M: Monad[M], I : (GitHub4s ~> M), A: ApplicativeError[M, Throwable]): M[GHResponse[A]] = gio foldMap I

    def liftGH: XorT[GHIO, GHException, GHResult[A]] = XorT[GHIO, GHException, GHResult[A]](gio)

  }


}