package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.free.domain.Collaborator

/** Users ops ADT
  */
sealed trait UserOp[A]
final case class GetUser(username: String) extends UserOp[Option[Collaborator]]
final case class GetAuthUser() extends UserOp[Collaborator]
final case class GetUsers() extends UserOp[List[Collaborator]]


/** Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class UserOps[F[_]](implicit I: Inject[UserOp, F]) {

  def getUser(username: String): Free[F, Option[Collaborator]] = Free.inject[UserOp, F](GetUser(username))

  def getAuthUser: Free[F, Collaborator] = Free.inject[UserOp, F](GetAuthUser())

  def getUsers: Free[F, List[Collaborator]] = Free.inject[UserOp, F](GetUsers())

}

/** Default implicit based DI factory from which instances of the UserOps may be obtained
  */
object UserOps {

  implicit def instance[F[_]](implicit I: Inject[UserOp, F]): UserOps[F] = new UserOps[F]

}