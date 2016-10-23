package github4s.integration

import cats.Id
import cats.implicits._
import github4s.Github._
import github4s.Github
import github4s.implicits._
import github4s.utils.TestUtils
import org.scalatest._

class GHAuthSpec extends FlatSpec with Matchers with TestUtils {

  "Auth >> NewAuth" should "return error on Left when invalid credential is provided" in {
    val response = Github().auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, invalidClientSecret).exec[Id]
    response should be('left)
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response = Github().auth.authorizeUrl(validClientId, validRedirectUri, validScopes).exec[Id]
    response should be('right)

    response.toOption map { r ⇒
      r.result.url.contains(validRedirectUri) shouldBe true
      r.statusCode shouldBe okStatusCode
    }

  }

  "Auth >> GetAccessToken" should "return error on Left for invalid code value" in {
    val response = Github().auth.getAccessToken(validClientId, invalidClientSecret, "", validRedirectUri, "").exec[Id]
    response should be('left)
  }

}
