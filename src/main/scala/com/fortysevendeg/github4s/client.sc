import cats.data.{XorT, Xor}
import cats.{Id, ApplicativeError, Applicative}
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.app._
import com.fortysevendeg.github4s.free.algebra.{RequestOps, RepositoryOps}
import com.fortysevendeg.github4s.free.domain.{Pagination, Commit}
import com.fortysevendeg.github4s.{GithubConfig, Github}
import com.fortysevendeg.github4s.free.interpreters.Interpreters
trait IdInstances {
  implicit def idApplicativeError(
      implicit
      I: Applicative[cats.Id]
  ): ApplicativeError[cats.Id, Throwable] = new ApplicativeError[cats.Id, Throwable] {

    import cats.Id

    override def pure[A](x: A): Id[A] = I.pure(x)

    override def ap[A, B](ff: Id[A ⇒ B])(fa: Id[A]): Id[B] = I.ap(ff)(fa)

    override def map[A, B](fa: Id[A])(f: Id[A ⇒ B]): Id[B] = I.map(fa)(f)

    override def product[A, B](fa: Id[A], fb: Id[B]): Id[(A, B)] = I.product(fa, fb)

    override def raiseError[A](e: Throwable): Id[A] =
      throw e

    override def handleErrorWith[A](fa: Id[A])(f: Throwable ⇒ Id[A]): Id[A] = {
      try {
        fa
      } catch {
        case e: Exception ⇒ f(e)
      }
    }
  }
}
object TestIdInterpreters extends Interpreters[cats.Id] with IdInstances
import com.fortysevendeg.github4s.Github._
import TestIdInterpreters._
implicit val config = GithubConfig(accessToken = Some("493b69d7141ef5baae678437891b439af26e80af"))

val api = Github


//Repo >> List Commits
val pag = Pagination(2, 5)
val r5 = api.repos.listCommits(
  owner = "scala-exercises",
  repo = "scala-exercises",
  sha = None,
  path = Some("site/build.sbt"),
  author = None,
  since = None,
  until = None,
  pagination = None).exec[Id]
val mynext5 = getNext(r5)
val r6 = api.repos.listCommits(
    owner = "scala-exercises",
    repo = "scala-exercises",
    sha = None,
    path = Some("site/build.sbt"),
    author = None,
    since = None,
    until = None,
    pagination = Some(pag)).exec[Id]
val mynext6 = getNext(r6)
def getLinks[A](response: GHResponse[A]): Option[String] = response match {
  case Xor.Right(GHListResult(result, status, headers, d)) => headers.get("link").map(_.toString)
  case Xor.Right(GHItemResult(result, status, headers)) => headers.get("link").map(_.toString)
  case Xor.Left(e) => Some(e.getMessage)
}
def getNext[A](response: GHResponse[A])(implicit O : RequestOps[GitHub4s]): Option[String] = response match {
  case Xor.Right(r:GHListResult[List[Commit]]) => {
    import TestIdInterpreters._
    O.nextList(r).map(_.exec[Id]).map(printResponse(_))
  }
  case _ => None
}
def printResponse[A](response: GHResponse[A]): String = response match {
  case Xor.Right(GHListResult(result, status, headers, d)) => result.toString
  case Xor.Right(GHItemResult(result, status, headers)) => result.toString
  case Xor.Left(e) => e.getMessage
}
