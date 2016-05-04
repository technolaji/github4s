package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.{GithubConfig, HttpClient}
import com.fortysevendeg.github4s.free.domain.{Pagination, Collaborator}
import io.circe.generic.auto._


object Users {

  protected val httpClient = new HttpClient()

  def get(accessToken: Option[String] = None, username: String): GHResponse[Collaborator] = httpClient.get[Collaborator](accessToken, s"users/$username")

  def getAuth(accessToken: Option[String] = None): GHResponse[Collaborator] = httpClient.get[Collaborator](accessToken, "user")

  def getUsers(
      accessToken: Option[String] = None,
      since: Int,
      pagination: Option[Pagination] = None) : GHResponse[List[Collaborator]] =
    httpClient.get[List[Collaborator]](accessToken, "users", Map("since" -> since.toString), pagination)


}
