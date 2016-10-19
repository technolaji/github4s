package github4s.integration

import cats.Id
import cats.implicits._
import github4s.Github._
import github4s.implicits._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest._

class GHGistsSpec extends FlatSpec with Matchers with TestUtils {
  "Gists >> Post" should "return the provided gist" in {
    val response = Github(accessToken)
      .gists
      .newGist(validGistDescription, validGistPublic, validGistFiles)
      .exec[Id]
    response should be('right)
    response.toOption map { r ⇒
      r.result.description shouldBe validGistDescription
      r.statusCode shouldBe createdStatusCode
    }
  }
}
