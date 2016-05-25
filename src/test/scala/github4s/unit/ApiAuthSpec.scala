package github4s.unit

import cats.scalatest.{ XorMatchers, XorValues }
import github4s.api.Auth
import github4s.utils.{ DummyGithubConfig, MockGithubApiServer, TestUtils }
import org.scalatest._

class ApiAuthSpec
  extends FlatSpec
  with Matchers
  with XorMatchers
  with XorValues
  with TestUtils
  with MockGithubApiServer
  with DummyGithubConfig {

  val auth = new Auth()

  "Auth >> NewAuth" should "return a valid token when valid credential is provided" in {
    val response = auth.newAuth(validUsername, "", validScopes, validNote, validClientId, "")
    response shouldBe right
    response.value.value.token.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when invalid credential is provided" in {
    val response = auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, "")
    response shouldBe left
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response = auth.authorizeUrl(validClientId, validRedirectUri, validScopes)
    response shouldBe right
    response.value.value.url.contains(validRedirectUri) shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  "Auth >> GetAccessToken" should "return a valid access_token when a valid code is provided" in {
    val response = auth.getAccessToken("", "", validCode, "", "")
    response shouldBe right
    response.value.value.access_token.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when invalid code is provided" in {
    val response = auth.getAccessToken("", "", invalidCode, "", "")
    response shouldBe left
  }

}
