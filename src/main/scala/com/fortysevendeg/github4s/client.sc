import cats.{Id, ApplicativeError, Applicative}
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

val api = com.fortysevendeg.github4s.Github()

val ops = for {
  a <- api.getUser("rafaparadela")
  b <- api.getUser("raulraja")
} yield (a, b)

val d = ops.exec[Id]
println(d._1.get.login)


val raul = api.getUser("raulraja").exec[Id].get.login


