package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.GithubTypes._
import com.fortysevendeg.github4s.free.domain.Collaborator

/** Users ops ADT
  */
sealed trait UserOp[A]
final case class GetUser(username: String) extends UserOp[GHResponse[Collaborator]]
final case class GetAuthUser() extends UserOp[GHResponse[Collaborator]]
final case class GetUsers(since: Int) extends UserOp[GHResponse[List[Collaborator]]]


/** Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class UserOps[F[_]](implicit I: Inject[UserOp, F]) {

  def getUser(username: String): Free[F, GHResponse[Collaborator]] = Free.inject[UserOp, F](GetUser(username))

  def getAuthUser: Free[F, GHResponse[Collaborator]] = Free.inject[UserOp, F](GetAuthUser())

  def getUsers(since: Int): Free[F, GHResponse[List[Collaborator]]] = Free.inject[UserOp, F](GetUsers(since))

}

/** Default implicit based DI factory from which instances of the UserOps may be obtained
  */
object UserOps {

  implicit def instance[F[_]](implicit I: Inject[UserOp, F]): UserOps[F] = new UserOps[F]

}