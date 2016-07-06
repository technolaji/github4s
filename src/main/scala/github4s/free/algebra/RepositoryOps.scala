package github4s.free.algebra

import cats.free.{ Free, Inject }
import github4s.GithubResponses.GHResponse
import github4s.free.domain.{ Commit, Pagination, Repository, User }

/**
  * Repositories ops ADT
  */
sealed trait RepositoryOp[A]
final case class GetRepo(
  owner: String,
  repo: String,
  accessToken: Option[String] = None
) extends RepositoryOp[GHResponse[Repository]]

final case class ListCommits(
  owner: String,
  repo: String,
  sha: Option[String] = None,
  path: Option[String] = None,
  author: Option[String] = None,
  since: Option[String] = None,
  until: Option[String] = None,
  pagination: Option[Pagination] = None,
  accessToken: Option[String] = None
) extends RepositoryOp[GHResponse[List[Commit]]]

final case class ListContributors(
  owner: String,
  repo: String,
  anon: Option[String] = None,
  accessToken: Option[String] = None
) extends RepositoryOp[GHResponse[List[User]]]

/**
  * Exposes Repositories operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class RepositoryOps[F[_]](implicit I: Inject[RepositoryOp, F]) {

  def getRepo(
    owner: String,
    repo: String,
    accessToken: Option[String] = None
  ): Free[F, GHResponse[Repository]] =
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
    accessToken: Option[String] = None
  ): Free[F, GHResponse[List[Commit]]] =
    Free.inject[RepositoryOp, F](ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken))

  def listContributors(
    owner: String,
    repo: String,
    anon: Option[String] = None,
    accessToken: Option[String] = None
  ): Free[F, GHResponse[List[User]]] =
    Free.inject[RepositoryOp, F](ListContributors(owner, repo, anon, accessToken))

}

/**
  * Default implicit based DI factory from which instances of the RepositoryOps may be obtained
  */
object RepositoryOps {

  implicit def instance[F[_]](implicit I: Inject[RepositoryOp, F]): RepositoryOps[F] = new RepositoryOps[F]

}