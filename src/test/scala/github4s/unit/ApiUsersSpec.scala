package github4s.unit

import cats.scalatest.{ XorMatchers, XorValues }
import github4s.api.Users
import github4s.utils.{ DummyGithubConfig, MockGithubApiServer, TestUtils }
import org.scalatest._

class ApiUsersSpec
  extends FlatSpec
  with Matchers
  with XorMatchers
  with XorValues
  with TestUtils
  with MockGithubApiServer
  with DummyGithubConfig {

  val users = new Users()

  "Users >> Get" should "return the expected login for a valid username" in {

    val response = users.get(accessToken, validUsername)

    response shouldBe right
    response.value.value.login shouldBe validUsername
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left for invalid username" in {
    val response = users.get(accessToken, invalidUsername)
    response shouldBe left
  }

  "Users >> GetAuth" should "return the expected login for a valid accessToken" in {
    val response = users.getAuth(accessToken)
    response shouldBe right
    response.value.value.login shouldBe validUsername
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when no accessToken is provided" in {
    val response = users.getAuth(None)
    response shouldBe left
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = users.getUsers(accessToken, validSinceInt)
    response shouldBe right
    response.value.value.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return an empty list when a invalid since value is provided" in {
    val response = users.getUsers(accessToken, invalidSinceInt)
    response shouldBe right
    response.value.value.isEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

}
