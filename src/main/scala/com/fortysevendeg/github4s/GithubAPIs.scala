package com.fortysevendeg.github4s

import com.fortysevendeg.github4s.GithubTypes.{GHResponse, GHIO}
import com.fortysevendeg.github4s.app._
import com.fortysevendeg.github4s.free.algebra.{RepositoryOps, RepositoryOp, UserOps}
import com.fortysevendeg.github4s.free.domain.{Commit, Repository, Collaborator}

class GHUsers(implicit O : UserOps[GitHub4s]){

  def get(username : String): GHIO[GHResponse[Collaborator]] = O.getUser(username)

  def getAuth: GHIO[GHResponse[Collaborator]] = O.getAuthUser

  def getUsers(since : Int): GHIO[GHResponse[List[Collaborator]]] = O.getUsers(since)

}

class GHRepos(implicit O : RepositoryOps[GitHub4s]){

  def get(owner : String, repo: String): GHIO[GHResponse[Repository]] = O.getRepo(owner, repo)

  def listCommits(
      owner : String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None): GHIO[GHResponse[List[Commit]]] =
    O.listCommits(owner, repo, sha, path, author, since, until)

}