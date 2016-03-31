package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.free.domain.Repository

/** Repositories ops ADT
  */
sealed trait RepositoryOp[A]
final case class ListYourRepos() extends RepositoryOp[List[Repository]]
final case class ListUserRepos() extends RepositoryOp[List[Repository]]
final case class ListOrganizationRepos() extends RepositoryOp[List[Repository]]


/** Exposes Repositories operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class RepositoryOps[F[_]](implicit I: Inject[RepositoryOp, F]) {

  def listYourRepos: Free[F, List[Repository]] = Free.inject[RepositoryOp, F](ListYourRepos())

  def listUserRepos: Free[F, List[Repository]] = Free.inject[RepositoryOp, F](ListUserRepos())

  def listOrganizationRepos: Free[F, List[Repository]] = Free.inject[RepositoryOp, F](ListOrganizationRepos())

}

/** Default implicit based DI factory from which instances of the RepositoryOps may be obtained
  */
object RepositoryOps {

  implicit def instance[F[_]](implicit I: Inject[RepositoryOp, F]): RepositoryOps[F] = new RepositoryOps[F]

}