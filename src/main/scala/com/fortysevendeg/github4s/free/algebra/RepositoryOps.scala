package com.fortysevendeg.github4s.free.algebra

import cats.free.{Free, Inject}
import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.free.domain.{Pagination, Commit, Repository}

/** Repositories ops ADT
  */
sealed trait RepositoryOp[A]
final case class GetRepo(
    owner: String,
    repo: String,
    accessToken: Option[String] = None) extends RepositoryOp[GHResponse[Repository]]

final case class ListCommits(
    owner: String,
    repo: String,
    sha: Option[String] = None,
    path: Option[String] = None,
    author: Option[String] = None,
    since: Option[String] = None,
    until: Option[String] = None,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None) extends RepositoryOp[GHResponse[List[Commit]]]


/** Exposes Repositories operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class RepositoryOps[F[_]](implicit I: Inject[RepositoryOp, F]) {

  def getRepo(
      owner: String,
      repo: String,
      accessToken: Option[String] = None): Free[F, GHResponse[Repository]] =
    Free.inject[RepositoryOp, F](GetRepo(owner, repo, accessToken))

  def listCommits(
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None): Free[F, GHResponse[List[Commit]]] =
    Free.inject[RepositoryOp, F](ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken))

}


/** Default implicit based DI factory from which instances of the RepositoryOps may be obtained
  */
object RepositoryOps {

  implicit def instance[F[_]](implicit I: Inject[RepositoryOp, F]): RepositoryOps[F] = new RepositoryOps[F]

}