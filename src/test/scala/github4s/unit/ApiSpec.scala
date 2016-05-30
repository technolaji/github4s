package github4s.unit

import cats.scalatest.{ XorMatchers, XorValues }
import github4s.api.{ Users, Repos, Auth }
import github4s.free.domain.Pagination
import github4s.utils.{ DummyGithubUrls, MockGithubApiServer, TestUtils }
import org.scalatest._

class ApiSpec
  extends FlatSpec
  with Matchers
  with XorMatchers
  with XorValues
  with TestUtils
  with MockGithubApiServer
  with DummyGithubUrls {

  val auth = new Auth
  val repos = new Repos
  val users = new Users

  "Auth >> NewAuth" should "return a valid token when valid credential is provided" in {
    val response = auth.newAuth(validUsername, "", validScopes, validNote, validClientId, "")
    response shouldBe right
    response.value.result.token.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when invalid credential is provided" in {
    val response = auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, "")
    response shouldBe left
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response = auth.authorizeUrl(validClientId, validRedirectUri, validScopes)
    response shouldBe right
    response.value.result.url.contains(validRedirectUri) shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  "Auth >> GetAccessToken" should "return a valid access_token when a valid code is provided" in {
    val response = auth.getAccessToken("", "", validCode, "", "")
    response shouldBe right
    response.value.result.access_token.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when invalid code is provided" in {
    val response = auth.getAccessToken("", "", invalidCode, "", "")
    response shouldBe left
  }

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response = repos.get(accessToken, validRepoOwner, validRepoName)
    response shouldBe right
    response.value.result.name shouldBe validRepoName
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" in {
    val response = repos.get(accessToken, validRepoOwner, invalidRepoName)
    response shouldBe left
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      owner       = validRepoOwner,
      repo        = validRepoName,
      pagination  = Option(Pagination(validPage, validPerPage))
    )
    response shouldBe right
    response.value.result.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return an empty list of commits for invalid page parameter" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      owner       = validRepoOwner,
      repo        = validRepoName,
      pagination  = Option(Pagination(invalidPage, validPerPage))
    )

    response shouldBe right
    response.value.result.isEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" in {
    val response = repos.listCommits(accessToken, validRepoOwner, invalidRepoName)
    response shouldBe left
  }

  "Users >> Get" should "return the expected login for a valid username" in {

    val response = users.get(accessToken, validUsername)

    response shouldBe right
    response.value.result.login shouldBe validUsername
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left for invalid username" in {
    val response = users.get(accessToken, invalidUsername)
    response shouldBe left
  }

  "Users >> GetAuth" should "return the expected login for a valid accessToken" in {
    val response = users.getAuth(accessToken)
    response shouldBe right
    response.value.result.login shouldBe validUsername
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when no accessToken is provided" in {
    val response = users.getAuth(None)
    response shouldBe left
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = users.getUsers(accessToken, validSinceInt)
    response shouldBe right
    response.value.result.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return an empty list when a invalid since value is provided" in {
    val response = users.getUsers(accessToken, invalidSinceInt)
    response shouldBe right
    response.value.result.isEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

}
