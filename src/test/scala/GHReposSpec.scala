import cats.Id
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.free.interpreters.IdInterpreters._
import com.fortysevendeg.github4s.Github
import com.fortysevendeg.github4s.Github._
import org.scalatest._
import cats.scalatest.{XorValues, XorMatchers}

class GHReposSpec extends FlatSpec with Matchers with XorMatchers with XorValues with TestUtils {

  "Repos >> Get" should "return the expected name when valid repo is provided" in {
    val response = Github().repos.get(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.entity.name shouldBe validRepoName
    response.value.statusCode shouldBe statusCodeOK
  }

  it should "return error on Left for invalid repo name" in {
    val response = Github().repos.get(validRepoOwner, invalidRepoName).exec[Id]
    response shouldBe left
  }

  "Repos >> ListCommits" should "return the expected login for a valid username" in {
    val response = Github().repos.listCommits(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.entity.nonEmpty shouldBe true
    response.value.statusCode shouldBe statusCodeOK
  }

  it should "return error on Left for invalid repo name" in {
    val response = Github().repos.listCommits(invalidRepoName, validRepoName).exec[Id]
    response shouldBe left
  }


}