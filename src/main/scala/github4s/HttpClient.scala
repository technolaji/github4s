package github4s

import github4s.GithubResponses.GHResponse
import github4s.free.domain.Pagination
import io.circe.Decoder
import scalaj.http._

class HttpClient(implicit config: GithubApiConfig) {

  val defaultPagination = Pagination(1, 1000)

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
    headers: Map[String, String] = Map.empty[String, String]
  ) {

    def postMethod = copy(httVerb = Post)

    def patchMethod = copy(httVerb = Patch)

    def putMethod = copy(httVerb = Put)

    def deleteMethod = copy(httVerb = Delete)

    def withAuth(accessToken: Option[String] = None) = copy(authHeader = accessToken match {
      case Some(token) ⇒ Map("Authorization" → s"token $token")
      case _ ⇒ Map.empty[String, String]
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

      println("####################################")
      println("####################################")
      println("####################################")
      println("####################################")
      println("####################################")
      println("####################################")
      println("####################################")
      println("####################################")
      println("####################################")
      println(config.getString("github.baseUrl"))
      println(request)
      println("####################################")

      data match {
        case Some(d) ⇒ request.postData(d).header("content-type", "application/json").asString
        case _ ⇒ request.asString
      }
    }

  }

  def get[A](
    accessToken: Option[String] = None,
    method: String,
    params: Map[String, String] = Map.empty,
    pagination: Option[Pagination] = None
  )(implicit D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(buildURL(method))
      .withAuth(accessToken)
      .withParams(params ++ pagination.fold(Map.empty[String, String])(p ⇒ Map("page" → p.page.toString, "per_page" → p.per_page.toString)))
      .run)

  def patch[A](accessToken: Option[String] = None, method: String, data: String)(implicit D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(buildURL(method))
      .patchMethod
      .withAuth(accessToken)
      .withData(data)
      .run)

  def put(accessToken: Option[String] = None, method: String): GHResponse[Unit] =
    GithubResponses.toEmpty(HttpRequestBuilder(buildURL(method))
      .putMethod
      .withAuth(accessToken)
      .withHeaders(Map("Content-Length" → "0"))
      .run)

  def post[A](
    accessToken: Option[String] = None,
    method: String,
    headers: Map[String, String] = Map.empty,
    data: String
  )(implicit D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(buildURL(method))
      .withAuth(accessToken)
      .withHeaders(headers)
      .withData(data)
      .run)

  def postAuth[A](
    method: String,
    headers: Map[String, String] = Map.empty,
    data: String
  )(implicit D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(buildURL(method))
      .withHeaders(headers)
      .withData(data)
      .run)

  def postOAuth[A](
    url: String,
    data: String
  )(implicit D: Decoder[A]): GHResponse[A] =
    GithubResponses.toEntity(HttpRequestBuilder(url)
      .withHeaders(Map("Accept" → "application/json"))
      .withData(data)
      .run)

  def delete(accessToken: Option[String] = None, method: String): GHResponse[Unit] =
    GithubResponses.toEmpty(HttpRequestBuilder(buildURL(method))
      .deleteMethod
      .withAuth(accessToken)
      .run)

  private val connTimeoutMs: Int = 1000
  private val readTimeoutMs: Int = 5000
  val defaultPage: Int = 1
  val defaultPerPage: Int = 30

  private def buildURL(method: String) = config.getString("github.baseUrl") + method

}
