package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.{GithubConfig, HttpClient}
import com.fortysevendeg.github4s.free.domain.{Pagination, Collaborator}


object Users {

  import io.circe.generic.auto._

  protected val httpClient = new HttpClient()

  def get(username: String)(implicit C : GithubConfig): GHResponse[Collaborator] = httpClient.get[Collaborator](s"users/$username")

  def getAuth(implicit C : GithubConfig): GHResponse[Collaborator] = httpClient.get[Collaborator]("user")

  def getUsers(since: Int, pagination: Option[Pagination] = None)
      (implicit C : GithubConfig): GHResponse[List[Collaborator]] = httpClient.get[List[Collaborator]]("users", Map("since" -> since.toString), pagination)


}
