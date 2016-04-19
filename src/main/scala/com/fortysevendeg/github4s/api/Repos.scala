package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.free.domain.{Pagination, Commit, Repository, Collaborator}
import com.fortysevendeg.github4s.{GithubConfig, HttpClient}
import io.circe.generic.auto._

object Repos {

  protected val httpClient = new HttpClient()

  def get(owner: String, repo: String)(implicit C : GithubConfig): GHResponse[Repository] =
    httpClient.get[Repository](s"repos/$owner/$repo")

  def listCommits(
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = None)(implicit C : GithubConfig): GHResponse[List[Commit]] =

    httpClient.get[List[Commit]](s"repos/$owner/$repo/commits", Map(
       "sha" -> sha,
       "path" -> path,
       "author" -> author,
       "since" -> since,
       "until" -> until).collect {
       case (key, Some(value)) => key -> value
     }, pagination)


}
