package github4s

import cats.data.{ OptionT, EitherT }
import cats.{ MonadError, ~>, RecursiveTailRecM }
import cats.implicits._
import github4s.GithubResponses._
import github4s.app._

/**
  * Represent the Github API wrapper
  * @param accessToken to identify the authenticated user
  */
class Github(accessToken: Option[String] = None) {

  lazy val users = new GHUsers(accessToken)
  lazy val repos = new GHRepos(accessToken)
  lazy val auth = new GHAuth(accessToken)
  lazy val gists = new GHGists(accessToken)

}

/** Companion object for [[github4s.Github]] */
object Github {

  def apply(accessToken: Option[String] = None) = new Github(accessToken)

  implicit class GithubIOSyntaxEither[A](gio: GHIO[GHResponse[A]]) {

    def exec[M[_]](implicit I: (GitHub4s ~> M), A: MonadError[M, Throwable], TR: RecursiveTailRecM[M]): M[GHResponse[A]] = gio foldMap I

    def liftGH: EitherT[GHIO, GHException, GHResult[A]] = EitherT[GHIO, GHException, GHResult[A]](gio)

  }
}
