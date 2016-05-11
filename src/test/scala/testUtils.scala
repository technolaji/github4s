trait TestUtils {
  val validUsername = "rafaparadela"
  val invalidUsername = "GHInvalidaUserName"
  val invalidPassword = "invalidPassword"

  val validScopes = List("public_repo")
  val validNote = "New access token"
  val validClientId = "e8e39175648c9db8c280"
  val invalidClientSecret = "1234567890"

  val validRepoOwner = "47deg"
  val validRepoName = "github4s"
  val invalidRepoName = "GHInvalidRepoName"
  val validRedirectUri = "http://localhost:9000/_oauth-callback"

  val validSinceInt = 100
  val invalidSinceInt = -1

  val statusCodeOK = 200
}
