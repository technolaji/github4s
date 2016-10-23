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

import io.circe.Decoder

import scala.concurrent.Future
import github4s.GithubResponses.GHResponse

import fr.hmil.roshttp.{HttpRequest, Method, HttpResponse}
import fr.hmil.roshttp.body.BodyPart

import java.nio.ByteBuffer

class HttpClient {

  type Headers = Map[String, String]

  def post[A](
      url: String,
      secretKey: String,
      method: String = "post",
      headers: Headers = Map.empty,
      data: String
  )(implicit D: Decoder[A]): Future[GHResponse[A]] =
    GithubResponses.toEntity(
      HttpRequestBuilder(url = url, httpVerb = method)
        .withHeaders(headers + (authHeaderName -> secretKey))
        .withBody(data)
        .run)

  def postAuth[A](
      method: String,
      headers: Map[String, String] = Map.empty,
      data: String
  )(implicit D: Decoder[A]): Future[GHResponse[A]] =
    GithubResponses.toEntity(
      HttpRequestBuilder(buildURL(method)).withHeaders(headers).withData(data).run)
}

case class HttpRequestBuilder(
    url: String,
    httpVerb: String,
    headers: Headers = Map.empty[String, String],
    body: String = ""
) {

  case class CirceJSONBody(value: String) extends BodyPart {
    override def contentType: String = s"application/json; charset=utf-8"

    override def content: ByteBuffer = ByteBuffer.wrap(value.getBytes("utf-8"))
  }

  def withHeaders(headers: Headers) = copy(headers = headers)

  def withBody(body: String) = copy(body = body)

  def run: Future[HttpResponse] = {

    val request = HttpRequest(url)
      .withMethod(Method(httpVerb))
      .withHeader("content-type", "application/json")
      .withHeaders(headers.toList: _*)

    request.send(CirceJSONBody(body))
  }
}
