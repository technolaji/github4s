package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.free.domain.Gist

/** Gists ops ADT
  */
sealed trait GistOp[A]
final case class GetGists() extends GistOp[List[Gist]]
final case class GetAuthGists() extends GistOp[List[Gist]]


/** Exposes Gists operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class GistOps[F[_]](implicit I: Inject[GistOp, F]) {

  def getGist: Free[F, List[Gist]] = Free.inject[GistOp, F](GetGists())

  def getAuthGist: Free[F, List[Gist]] = Free.inject[GistOp, F](GetAuthGists())

}

/** Default implicit based DI factory from which instances of the GistOps may be obtained
  */
object GistOps {

  implicit def instance[F[_]](implicit I: Inject[GistOp, F]): GistOps[F] = new GistOps[F]

}