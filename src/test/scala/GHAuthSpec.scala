import cats.Id
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.free.interpreters.IdInterpreters._
import com.fortysevendeg.github4s.Github
import com.fortysevendeg.github4s.Github._
import org.scalatest._
import cats.scalatest.{XorValues, XorMatchers}

class GHAuthSpec extends FlatSpec with Matchers with XorMatchers with XorValues with TestUtils {

  "Auth >> NewAuth" should "return error on Left when invalid credential is provided" in {
    val response = Github().auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, invalidClientSecret).exec[Id]
    response shouldBe left
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response = Github().auth.authorizeUrl(validClientId, validRedirectUri, validScopes).exec[Id]
    response shouldBe right
    response.value.value.url.contains(validRedirectUri) shouldBe true
    response.value.statusCode shouldBe statusCodeOK
  }

  "Auth >> GetAccessToken" should "return error on Left for invalid code value" in {
    val response = Github().auth.getAccessToken(validClientId, invalidClientSecret, "", validRedirectUri, "").exec[Id]
    response shouldBe left
  }

}
