package github4s

import cats.free.Free
import cats.implicits._
import github4s.app.GitHub4s
import io.circe.Decoder
import io.circe.parser._
import io.circe.generic.auto._
import scalaj.http.HttpResponse

object GithubResponses {

  type GHIO[A] = Free[GitHub4s, A]

  type GHResponse[A] = Either[GHException, GHResult[A]]

  case class GHResult[A](result: A, statusCode: Int, headers: Map[String, IndexedSeq[String]])

  sealed abstract class GHException(msg: String, cause: Option[Throwable] = None) extends Throwable(msg) {
    cause foreach initCause
  }

  case class JsonParsingException(
    msg: String,
    json: String
  ) extends GHException(msg)

  case class UnexpectedException(msg: String) extends GHException(msg)

  def toEntity[A](response: HttpResponse[String])(implicit D: Decoder[A]): GHResponse[A] = response match {
    case r if r.isSuccess ⇒ decode[A](r.body)
      .fold(
        e ⇒ Either.left(JsonParsingException(e.getMessage, r.body)),
        result ⇒ Either.right(GHResult(result, r.code, toLowerCase(r.headers)))
      )
    case r ⇒ Either.left(UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}"))
  }

  def toEmpty(response: HttpResponse[String]): GHResponse[Unit] = response match {
    case r if r.isSuccess ⇒ Either.right(GHResult(Unit, r.code, toLowerCase(r.headers)))
    case r ⇒ Either.left(UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}"))
  }

  private def toLowerCase(headers: Map[String, IndexedSeq[String]]): Map[String, IndexedSeq[String]] = headers.map(e ⇒ (e._1.toLowerCase, e._2))

}
