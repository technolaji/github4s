/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.utils

import cats.Id
import github4s.GithubResponses.{GHResponse, UnexpectedException}
import github4s.app.GitHub4s
import github4s.free.algebra._
import github4s.free.domain.Pagination
import github4s.{HttpClient, HttpRequestBuilder, HttpRequestBuilderExtension, IdInstances}
import io.circe
import io.circe.Decoder
import io.circe.parser.parse
import org.scalamock.matchers.MockParameter
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

trait BaseSpec extends FlatSpec with Matchers with TestData with IdInstances with MockFactory {

  case class JsonMockParameter(json: String) extends MockParameter[String](json) {
    override def equals(argument: Any): Boolean = parse(json) == parse(argument.toString)
  }

  class HttpClientTest extends HttpClient[String, Id]

  implicit def httpRequestBuilderExtension: HttpRequestBuilderExtension[String, Id] =
    new HttpRequestBuilderExtension[String, Id] {
      override def run[A](rb: HttpRequestBuilder[String, Id])(
          implicit D: circe.Decoder[A]): Id[GHResponse[A]] =
        Left(UnexpectedException("Stub!"))

      override def runEmpty(rb: HttpRequestBuilder[String, Id]): Id[GHResponse[Unit]] =
        Left(UnexpectedException("Stub!"))
    }

  def httpClientMockGet[T](
      url: String,
      params: Map[String, String] = Map.empty,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .get[T](
        _: Option[String],
        _: String,
        _: Map[String, String],
        _: Map[String, String],
        _: Option[Pagination])(_: Decoder[T]))
      .expects(sampleToken, url, headerUserAgent, params, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPost[T](
      url: String,
      json: String,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .post[T](_: Option[String], _: String, _: Map[String, String], _: String)(_: Decoder[T]))
      .expects(sampleToken, url, headerUserAgent, JsonMockParameter(json), *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPostAuth[T](
      url: String,
      headers: Map[String, String],
      json: String,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postAuth[T](_: String, _: Map[String, String], _: String)(_: Decoder[T]))
      .expects(url, headers ++ headerUserAgent, JsonMockParameter(json), *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPostOAuth[T](
      url: String,
      json: String,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postOAuth[T](_: String, _: Map[String, String], _: String)(_: Decoder[T]))
      .expects(url, headerUserAgent, JsonMockParameter(json), *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPatch[T](
      url: String,
      json: String,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .patch[T](_: Option[String], _: String, _: Map[String, String], _: String)(_: Decoder[T]))
      .expects(sampleToken, url, headerUserAgent, JsonMockParameter(json), *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPut[T](
      url: String,
      json: String,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .put[T](_: Option[String], _: String, _: Map[String, String], _: String)(_: Decoder[T]))
      .expects(sampleToken, url, headerUserAgent, JsonMockParameter(json), *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDelete(url: String, response: GHResponse[Unit]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .delete(_: Option[String], _: String, _: Map[String, String]))
      .expects(sampleToken, url, headerUserAgent)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDeleteWithResponse[T](
      url: String,
      response: GHResponse[T]): HttpClient[String, Id] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .deleteWithResponse[T](
        _: Option[String],
        _: String,
        _: Map[String, String])(_: Decoder[T]))
      .expects(sampleToken, url, headerUserAgent, *)
      .returns(response)
    httpClientMock
  }

  class GitDataOpsTest      extends GitDataOps[GitHub4s]
  class PullRequestOpsTest  extends PullRequestOps[GitHub4s]
  class RepositoryOpsTest   extends RepositoryOps[GitHub4s]
  class IssueOpsTest        extends IssueOps[GitHub4s]
  class ActivityOpsTest     extends ActivityOps[GitHub4s]
  class AuthOpsTest         extends AuthOps[GitHub4s]
  class UserOpsTest         extends UserOps[GitHub4s]
  class GistOpsTest         extends GistOps[GitHub4s]
  class OrganizationOpsTest extends OrganizationOps[GitHub4s]
  class WebhookOpsTest      extends WebhookOps[GitHub4s]

}
