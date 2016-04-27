package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.free.domain.Authorization

/** Auths ops ADT
  */
sealed trait AuthOp[A]
final case class NewAuth(
    username: String,
    password: String,
    scopes: List[String],
    note: String,
    client_id: String,
    client_secret: String) extends AuthOp[GHResponse[Authorization]]


/** Exposes Auths operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class AuthOps[F[_]](implicit I: Inject[AuthOp, F]) {

  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String): Free[F, GHResponse[Authorization]] =
    Free.inject[AuthOp, F](NewAuth(username, password, scopes, note, client_id, client_secret))

}

/** Default implicit based DI factory from which instances of the AuthOps may be obtained
  */
object AuthOps {

  implicit def instance[F[_]](implicit I: Inject[AuthOp, F]): AuthOps[F] = new AuthOps[F]

}