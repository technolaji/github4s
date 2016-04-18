package com.fortysevendeg.github4s

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import io.circe.Decoder
import scalaj.http._


class HttpClient {

  sealed trait HttpVerb {
    def verb: String
  }

  case object Get extends HttpVerb {
    def verb = "GET"
  }

  case object Post extends HttpVerb {
    def verb = "POST"
  }

  case object Put extends HttpVerb {
    def verb = "PUT"
  }

  case object Delete extends HttpVerb {
    def verb = "DELETE"
  }

  case object Patch extends HttpVerb {
    def verb = "PATCH"
  }

  case class HttpRequestBuilder(
      url: String,
      httVerb: HttpVerb = Get,
      connectionTimeout: Int = connTimeoutMs,
      readTimeOut: Int = readTimeoutMs,
      authHeader: Map[String, String] = Map.empty[String, String],
      data: Option[String] = None,
      params: Map[String, String] = Map.empty[String, String],
      headers: Map[String, String] = Map.empty[String, String]) {

    def postMethod = copy(httVerb = Post)

    def patchMethod = copy(httVerb = Patch)

    def putMethod = copy(httVerb = Put)

    def deleteMethod = copy(httVerb = Delete)

    def withAuth(accessToken: Option[String] = None) = copy(authHeader = accessToken match {
      case Some(token) => Map("Authorization" -> s"token $token")
      case _ => Map.empty[String, String]
    })

    def withHeaders(headers: Map[String, String]) = copy(headers = headers)

    def withParams(params: Map[String, String]) = copy(params = params)

    def withData(data: String) = copy(data = Option(data))

    def run = {
      val request = Http(url)
          .method(httVerb.verb)
          .option(HttpOptions.connTimeout(connectionTimeout))
          .option(HttpOptions.readTimeout(readTimeOut))
          .params(params)
          .headers(authHeader)
          .headers(headers)

      data match {
        case Some(d) => request.postData(d).header("content-type", "application/json").asString
        case _ => request.asString
      }
    }

  }


  def get[A](method: String, params: Map[String, String] = Map.empty)(implicit C: GithubConfig, D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(buildURL(method))
        .withAuth(C.accessToken)
        .withParams(params)
        .run, D)

  def getByUrl[A](url: String, d: Decoder[A])(implicit C: GithubConfig): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(url)
        .withAuth(C.accessToken)
        .run, d)


  def patch[A](method: String, data: String)(implicit C: GithubConfig, D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(buildURL(method))
        .patchMethod
        .withAuth(C.accessToken)
        .withData(data)
        .run, D)

  def put(method: String)(implicit C: GithubConfig): GHResponse[Unit] =
    GithubResponses.toEmpty(HttpRequestBuilder(buildURL(method))
        .putMethod
        .withAuth(C.accessToken)
        .withHeaders(Map("Content-Length" -> "0"))
        .run)

  def delete(method: String)(implicit C: GithubConfig): GHResponse[Unit] =
    GithubResponses.toEmpty(HttpRequestBuilder(buildURL(method))
        .deleteMethod
        .withAuth(C.accessToken)
        .run)


  private val connTimeoutMs: Int = 1000
  private val readTimeoutMs: Int = 5000

  private def buildURL(method: String) = s"https://api.github.com/$method"


}
