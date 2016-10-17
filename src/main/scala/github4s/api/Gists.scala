package github4s.api

import github4s.free.domain._
import github4s.{ Decoders, GithubApiUrls, HttpClient }
import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._

/** Factory to encapsulate calls related to Repositories operations  */
class Gists(implicit urls: GithubApiUrls) {
  import Decoders._

  val httpClient = new HttpClient

  /**
    * Create a new gist
    *
    * @param description of the gist
    * @param public boolean value that describes if the Gist should be public or not
    * @param files map describing the filenames of the Gist and its contents
    * @param accessToken to identify the authenticated user
    */
  def newGist(description: String, public: Boolean, files: Map[String, GistFile], accessToken: Option[String] = None) =
    httpClient.post[Gist](
      accessToken,
      "gists",
      data = NewGistRequest(description, public, files).asJson.noSpaces
    )
}