package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.GithubResponses._
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

  def nextList[A](result: GHListResult[A]): Option[Free[F, GHResponse[A]]] = followLink[A](result, "next")

  def followLink[A](result: GHListResult[A], rel: String): Option[Free[F, GHResponse[A]]] = result match {
    case GHListResult(_, _, headers, decoder) => {
      for {
        linkHeader <- headers.get("link")
        nextLink <- extractLink(linkHeader).get(rel)
      } yield next[A](nextLink, decoder)
    }
    case _ => None
  }

  private def extractLink(rawLink : IndexedSeq[String]): Map[String, String] =
    rawLink.flatMap(_.split(",").map(l => {
        val Array(rawUrl, rawRel) = l.split(";")
        val url = rawUrl.substring(1, rawUrl.length - 1)
        val rel = rawRel.split("\"").last
        (rel, url)
      }).toMap).toMap

}

/** Default implicit based DI factory from which instances of the RequestOps may be obtained
  */
object RequestOps {

  implicit def instance[F[_]](implicit I: Inject[RequestOp, F]): RequestOps[F] = new RequestOps[F]

}