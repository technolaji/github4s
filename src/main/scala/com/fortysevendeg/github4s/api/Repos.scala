package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.free.domain.{Pagination, Commit, Repository, User}
import com.fortysevendeg.github4s.{Decoders, GithubConfig, HttpClient}
import io.circe.generic.auto._

object Repos {

  import Decoders._

  protected val httpClient = new HttpClient()

  def get(accessToken: Option[String] = None, owner: String, repo: String): GHResponse[Repository] =
    httpClient.get[Repository](accessToken, s"repos/$owner/$repo")

  def listCommits(
      accessToken: Option[String] = None,
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)): GHResponse[List[Commit]] =

    httpClient.get[List[Commit]](accessToken, s"repos/$owner/$repo/commits", Map(
       "sha" -> sha,
       "path" -> path,
       "author" -> author,
       "since" -> since,
       "until" -> until).collect {
       case (key, Some(value)) => key -> value
     }, pagination)


}
