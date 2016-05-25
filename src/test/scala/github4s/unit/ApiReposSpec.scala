package github4s.unit

import cats.scalatest.{ XorMatchers, XorValues }
import github4s.api.Repos
import github4s.free.domain.Pagination
import github4s.utils.{ DummyGithubConfig, MockGithubApiServer, TestUtils }
import org.scalatest._

class ApiReposSpec
  extends FlatSpec
  with Matchers
  with XorMatchers
  with XorValues
  with TestUtils
  with MockGithubApiServer
  with DummyGithubConfig {

  val repos = new Repos

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response = repos.get(accessToken, validRepoOwner, validRepoName)
    response shouldBe right
    response.value.value.name shouldBe validRepoName
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
    response.value.value.nonEmpty shouldBe true
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
    response.value.value.isEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" in {
    val response = repos.listCommits(accessToken, validRepoOwner, invalidRepoName)
    response shouldBe left
  }

}
