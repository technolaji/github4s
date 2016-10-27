/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s

import scala.concurrent.Future
import fr.hmil.roshttp._
import fr.hmil.roshttp.body.{BodyPart, BulkBodyPart}
import java.nio.ByteBuffer

import cats.implicits._
import fr.hmil.roshttp.response.SimpleHttpResponse
import fr.hmil.roshttp.util.HeaderMap
import fr.hmil.roshttp.body.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import github4s.GithubResponses.{GHResponse, GHResult, JsonParsingException, UnexpectedException}
import github4s.GithubDefaultUrls._
import github4s.Decoders._
import io.circe.Decoder
import io.circe.parser._
import monix.reactive.Observable

case class CirceJSONBody(value: String) extends BulkBodyPart {
  override def contentType: String     = s"application/json; charset=utf-8"
  override def contentData: ByteBuffer = ByteBuffer.wrap(value.getBytes("utf-8"))
}

trait HttpRequestBuilderExtensionJS {

  import monix.execution.Scheduler.Implicits.global

  implicit def extensionJS: HttpRequestBuilderExtension[SimpleHttpResponse, Future] =
    new HttpRequestBuilderExtension[SimpleHttpResponse, Future] {
      def run[A](rb: HttpRequestBuilder[SimpleHttpResponse, Future])(
          implicit D: Decoder[A]): Future[GHResponse[A]] = {
        val request = HttpRequest(rb.url)
          .withMethod(Method(rb.httpVerb.verb))
          .withQueryParameters(rb.params.toSeq: _*)
          .withHeader("content-type", "application/json")
          .withHeaders(rb.authHeader.toList: _*)
          .withHeaders(rb.headers.toList: _*)

        rb.data match {
          case Some(d) ⇒ {
            println(
              s"request method: ${request.method}, request headers: ${request.headers}, url: ${request.url}")
            request
              .withHeader("content-type", "application/json")
              .send(CirceJSONBody(d))
              .map(r => toEntity[A](r))
          }
          case _ ⇒ request.send().map(r => toEntity[A](r))
        }
      }
    }

  def toEntity[A](response: SimpleHttpResponse)(implicit D: Decoder[A]): GHResponse[A] =
    response match {
      case r if r.statusCode < 400 ⇒
        decode[A](r.body).fold(
          e ⇒ Either.left(JsonParsingException(e.getMessage, r.body)),
          result ⇒
            Either.right(
              GHResult(result, r.statusCode, rosHeaderMapToRegularMap(r.headers))
          )
        )
      case r ⇒
        Either.left(
          UnexpectedException(
            s"Failed invoking get with status : ${r.statusCode}, body : \n ${r.body}"))
    }

  def toEmpty(response: SimpleHttpResponse): GHResponse[Unit] = response match {
    case r if r.statusCode < 400 ⇒
      Either.right(GHResult(Unit, r.statusCode, rosHeaderMapToRegularMap(r.headers)))
    case r ⇒
      Either.left(
        UnexpectedException(
          s"Failed invoking get with status : ${r.statusCode}, body : \n ${r.body}"))
  }

  private def rosHeaderMapToRegularMap(
      headers: HeaderMap[String]): Map[String, IndexedSeq[String]] =
    headers.flatMap(m => Map(m._1.toLowerCase -> IndexedSeq(m._2)))

}
