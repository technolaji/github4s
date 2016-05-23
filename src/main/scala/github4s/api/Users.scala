package github4s.api

import github4s.GithubResponses.GHResponse
import github4s.HttpClient
import github4s.free.domain.{ Pagination, User }
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Users operations  */
object Users {

  protected val httpClient = new HttpClient()

  /**
    * Get information for a particular user
    *
    * @param accessToken to identify the authenticated user
    * @param username of the user to retrieve
    * @return GHResponse[User] User details
    */
  def get(accessToken: Option[String] = None, username: String): GHResponse[User] = httpClient.get[User](accessToken, s"users/$username")

  /**
    * Get information of the authenticated user
    * @param accessToken to identify the authenticated user
    * @return GHResponse[User] User details
    */
  def getAuth(accessToken: Option[String] = None): GHResponse[User] = httpClient.get[User](accessToken, "user")

  /**
    * Get users
    *
    * @param accessToken to identify the authenticated user
    * @param since The integer ID of the last User that you've seen.
    * @param pagination Limit and Offset for pagination
    * @return GHResponse[List[User] ] List of user's details
    */
  def getUsers(
    accessToken: Option[String] = None,
    since: Int,
    pagination: Option[Pagination] = None
  ): GHResponse[List[User]] =
    httpClient.get[List[User]](accessToken, "users", Map("since" → since.toString), pagination)

}
