package github4s.integration

import cats.Id
import cats.implicits._
import github4s.Github._
import github4s.implicits._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest._

class GHUsersSpec extends FlatSpec with Matchers with TestUtils {

  "Users >> Get" should "return the expected login for a valid username" in {
    val response = Github(accessToken).users.get(validUsername).exec[Id]
    response should be('right)
    response.toOption map { r ⇒
      r.result.login shouldBe validUsername
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error on Left for invalid username" in {
    val response = Github(accessToken).users.get(invalidUsername).exec[Id]
    response should be('left)
  }

  "Users >> GetAuth" should "return error on Left when no accessToken is provided" in {
    val response = Github().users.getAuth.exec[Id]
    response should be('left)
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = Github(accessToken).users.getUsers(validSinceInt).exec[Id]
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return an empty list when a invalid since value is provided" in {
    val response = Github(accessToken).users.getUsers(invalidSinceInt).exec[Id]
    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

}
