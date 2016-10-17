package github4s.free.algebra

import cats.free.{ Free, Inject }
import github4s.GithubResponses._
import github4s.free.domain.{ Gist, GistFile }

/**
  * Gist ops ADT
  */
sealed trait GistOp[A]

final case class NewGist(
  description: String,
  public: Boolean,
  files: Map[String, GistFile],
  accessToken: Option[String] = None
) extends GistOp[GHResponse[Gist]]

/**
  * Exposes Gists operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */

class GistOps[F[_]](implicit I: Inject[GistOp, F]) {
  def newGist(
    description: String,
    public: Boolean,
    files: Map[String, GistFile],
    accessToken: Option[String] = None
  ): Free[F, GHResponse[Gist]] =
    Free.inject[GistOp, F](NewGist(description, public, files))
}

/**
  * Default implicit based DI factory from which instances of the GistOps may be obtained
  */

object GistOps {

  implicit def instance[F[_]](implicit I: Inject[GistOp, F]): GistOps[F] = new GistOps[F]

}