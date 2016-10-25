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
import fr.hmil.roshttp.{HttpRequest, HttpResponse, Method}
import fr.hmil.roshttp.body.BodyPart
import java.nio.ByteBuffer

import github4s.GithubResponses.GHResponse
import github4s.HttpClient.Headers

case class CirceJSONBody(value: String) extends BodyPart {
  override def contentType: String = s"application/json; charset=utf-8"
  override def content: ByteBuffer = ByteBuffer.wrap(value.getBytes("utf-8"))
}

object HttpClientExtensionJS {

  implicit object ExtensionJVM extends HttpClientExtension[GHResponse[HttpResponse]] {
    def run(rb: HttpRequestBuilder): Future[GHResponse[HttpResponse]] = {
      val request = HttpRequest(rb.url)
        .withMethod(Method(rb.httpVerb.verb))
        .withHeader("content-type", "application/json")
        .withHeaders(rb.headers.toList: _*)

      rb.data match {
        case Some(d) ⇒ request.send(CirceJSONBody(d)).map(r => GithubResponses.toEntity(r))
        case _       ⇒ request.send().map(r => r)
      }
    }
  }

}
