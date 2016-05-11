package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.HttpClient
import com.fortysevendeg.github4s.free.domain.{Pagination, User}
import io.circe.generic.auto._


object Users {

  protected val httpClient = new HttpClient()

  def get(accessToken: Option[String] = None, username: String): GHResponse[User] = httpClient.get[User](accessToken, s"users/$username")

  def getAuth(accessToken: Option[String] = None): GHResponse[User] = httpClient.get[User](accessToken, "user")

  def getUsers(
      accessToken: Option[String] = None,
      since: Int,
      pagination: Option[Pagination] = None) : GHResponse[List[User]] =
    httpClient.get[List[User]](accessToken, "users", Map("since" -> since.toString), pagination)


}
