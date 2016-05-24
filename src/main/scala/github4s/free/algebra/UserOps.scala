package github4s.free.algebra

import cats.free.{ Free, Inject }
import github4s.GithubResponses._
import github4s.free.domain.{ Pagination, User }

/**
  * Users ops ADT
  */
sealed trait UserOp[A]
final case class GetUser(username: String, accessToken: Option[String] = None) extends UserOp[GHResponse[User]]
final case class GetAuthUser(accessToken: Option[String] = None) extends UserOp[GHResponse[User]]
final case class GetUsers(since: Int, pagination: Option[Pagination] = None, accessToken: Option[String] = None) extends UserOp[GHResponse[List[User]]]

/**
  * Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class UserOps[F[_]](implicit I: Inject[UserOp, F]) {

  def getUser(username: String, accessToken: Option[String] = None): Free[F, GHResponse[User]] = Free.inject[UserOp, F](GetUser(username, accessToken))

  def getAuthUser(accessToken: Option[String] = None): Free[F, GHResponse[User]] = Free.inject[UserOp, F](GetAuthUser(accessToken))

  def getUsers(since: Int, pagination: Option[Pagination] = None, accessToken: Option[String] = None): Free[F, GHResponse[List[User]]] = Free.inject[UserOp, F](GetUsers(since, pagination, accessToken))

}

/**
  * Default implicit based DI factory from which instances of the UserOps may be obtained
  */
object UserOps {

  implicit def instance[F[_]](implicit I: Inject[UserOp, F]): UserOps[F] = new UserOps[F]

}