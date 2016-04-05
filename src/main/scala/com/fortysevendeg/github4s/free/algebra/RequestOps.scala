package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.GithubResponses.GHListResult
import com.fortysevendeg.github4s.GithubTypes.GHResponse
import io.circe.Decoder

/** Requests ops ADT
  */
sealed trait RequestOp[A]
final case class Next[A](url: String, decoder: Decoder[A]) extends RequestOp[GHResponse[A]]


/** Exposes Requests operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class RequestOps[F[_]](implicit I: Inject[RequestOp, F]) {

  def next[A](url: String, decoder: Decoder[A]): Free[F, GHResponse[A]] = Free.inject[RequestOp, F](Next[A](url, decoder))

  def nextList[A](result: GHListResult[A]): Option[Free[F, GHResponse[A]]] = result match {
    case GHListResult(_, _, _, decoder) => //TODO check link
      Option(next[A]("https://api.github.com/repositories/23613922/commits?path=site%2Fbuild.sbt&per_page=10&page=2", decoder))
    case _ => None
  }


}

/** Default implicit based DI factory from which instances of the RequestOps may be obtained
  */
object RequestOps {

  implicit def instance[F[_]](implicit I: Inject[RequestOp, F]): RequestOps[F] = new RequestOps[F]

}