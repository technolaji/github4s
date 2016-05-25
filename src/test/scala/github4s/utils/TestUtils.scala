package github4s.utils

import scalaj.http.HttpConstants._

trait TestUtils {

  val accessToken = sys.props.get("token")
  def tokenHeader = "token " + accessToken.getOrElse("")

  val validUsername = "rafaparadela"
  val invalidUsername = "GHInvalidaUserName"
  val invalidPassword = "invalidPassword"

  def validBasicAuth = s"Basic ${base64(s"$validUsername:")}"
  def invalidBasicAuth = s"Basic ${base64(s"$validUsername:$invalidPassword")}"

  val validScopes = List("public_repo")
  val validNote = "New access token"
  val validClientId = "e8e39175648c9db8c280"
  val invalidClientSecret = "1234567890"
  val validCode = "code"
  val invalidCode = "invalid-code"

  val validRepoOwner = "47deg"
  val validRepoName = "github4s"
  val invalidRepoName = "GHInvalidRepoName"
  val validRedirectUri = "http://localhost:9000/_oauth-callback"
  val validPage = 1
  val invalidPage = 999
  val validPerPage = 100

  val validSinceInt = 100
  val invalidSinceInt = 999999999

  val okStatusCode = 200
  val unauthorizedStatusCode = 401
  val notFoundStatusCode = 404

}
