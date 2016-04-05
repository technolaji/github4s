import cats.data.{XorT, Xor}
import cats.{Id, ApplicativeError, Applicative}
import com.fortysevendeg.github4s.GithubResponses.{GHItemResult, GHListResult}
import com.fortysevendeg.github4s.GithubTypes.{GHIO, GHResponse}
import com.fortysevendeg.github4s.app._
import com.fortysevendeg.github4s.free.algebra.{RequestOps, RepositoryOps}
import com.fortysevendeg.github4s.free.domain.Commit
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
//implicit val config = GithubConfig()

val api = Github



//Users >> Get User
//val r1 = api.users.get("raulraja").exec[Id]
//printResponse(r1)



//Users >> Get Auth User
//val r2 = api.users.getAuth.exec[Id]
//printResponse(r2)




//Users >> Get Users
//val r3 = api.users.getUsers(100).exec[Id]
//printResponse(r3)






//Repos >> Get Repo
//val r4 = api.repos.get("scala-exercises", "scala-exercises").exec[Id]
//printResponse(r4)

//Repo >> List Commits
//val r5 = api.repos.listCommits("scala-exercises", "scala-exercises", None, Some("site/build.sbt")).exec[Id]
//getLinks(r5)
//val st = getNext(r5)



////Monadic composition
//val ops = for {
//  a <- api.users.get("rafaparadela").liftGH
//  b <- api.users.get("raulraja").liftGH
//} yield a.login + b.login
//
//val s = ops.value.exec[Id]




def getLinks[A](response: GHResponse[A]): Option[String] = response match {
  case Xor.Right(GHListResult(result, status, headers, d)) => headers.get("link").map(_.toString)
  case Xor.Right(GHItemResult(result, status, headers)) => headers.get("link").map(_.toString)
  case Xor.Left(e) => Some(e.getMessage)
}
def getNext[A](response: GHResponse[A])(implicit O : RequestOps[GitHub4s]): Option[String] = response match {
  case Xor.Right(r:GHListResult[List[Commit]]) => {
    val a = O.nextList(r).get
  println("A: " + a)
    import TestIdInterpreters._
    val c = a.exec[Id]

    Option(printResponse(c))
  }
  case _ => None
}
def printResponse[A](response: GHResponse[A]): String = response match {
  case Xor.Right(GHListResult(result, status, headers, d)) => result.toString
  case Xor.Right(GHItemResult(result, status, headers)) => result.toString
  case Xor.Left(e) => e.getMessage
}

