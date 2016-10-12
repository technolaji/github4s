package github4s.integration

import cats.Id
import cats.scalatest.{ XorMatchers, XorValues }
import github4s.Github._
import github4s.GithubResponses._
import github4s.testimplicits._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest.{ Matchers, FlatSpec }

class GHReposSpec extends FlatSpec with Matchers with XorMatchers with XorValues with TestUtils {

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response = Github(accessToken).repos.get(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.result.name shouldBe validRepoName
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" in {
    val response = Github(accessToken).repos.get(validRepoOwner, invalidRepoName).exec[Id]
    response shouldBe left
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = Github(accessToken).repos.listCommits(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.result.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" in {
    val response = Github(accessToken).repos.listCommits(invalidRepoName, validRepoName).exec[Id]
    response shouldBe left
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response = Github(accessToken).repos.listContributors(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.result shouldNot be(empty)
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" in {
    val response = Github(accessToken).repos.listContributors(invalidRepoName, validRepoName).exec[Id]
    response shouldBe left
  }

}
