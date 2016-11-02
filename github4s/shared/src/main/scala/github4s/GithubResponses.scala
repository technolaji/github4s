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

import cats.free.Free
import github4s.app.GitHub4s

object GithubResponses {

  type GHIO[A] = Free[GitHub4s, A]

  type GHResponse[A] = Either[GHException, GHResult[A]]

  case class GHResult[A](result: A, statusCode: Int, headers: Map[String, IndexedSeq[String]])

  sealed abstract class GHException(msg: String, cause: Option[Throwable] = None)
      extends Throwable(msg) {
    cause foreach initCause
  }

  case class JsonParsingException(
      msg: String,
      json: String
  ) extends GHException(msg)

  case class UnexpectedException(msg: String) extends GHException(msg)

}
