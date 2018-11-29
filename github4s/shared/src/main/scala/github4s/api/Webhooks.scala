package github4s.api

import github4s.GithubResponses.GHResponse
import github4s.free.domain._
import github4s.free.interpreters.Capture
import github4s.{Decoders, Encoders, GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.util.URLEncoder
import io.circe.generic.auto._
import io.circe.syntax._

/** Factory to encapsulate calls related to Users operations  */
class Webhooks[C, M[_]](
                         implicit urls: GithubApiUrls,
                         C: Capture[M],
                         httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  val httpClient = new HttpClient[C, M]

  /**
    * Get list of webhooks for a user
    *
    * @param accessToken to identify the authenticated user
    * @param headers     optional user headers to include in the request
    * @param owner    of the user to retrieve
    * @return GHResponse[User] User details
    */
  def listWebhooks(
                    accessToken: Option[String] = None,
                    headers: Map[String, String] = Map(),
                    owner: String,
                    repo: String): M[GHResponse[List[Webhook]]] =
    httpClient.get[List[Webhook]](accessToken, s"repos/$owner/$repo/hooks", headers)

}
