package com.fortysevendeg.github4s.free.interpreters

import cats.{Monad, MonadError}

trait IdInstances {
  implicit def idMonadError(
      implicit
      I: Monad[cats.Id]
  ): MonadError[cats.Id, Throwable] = new MonadError[cats.Id, Throwable] {

    import cats.Id

    override def pure[A](x: A): Id[A] = I.pure(x)

    override def ap[A, B](ff: Id[A ⇒ B])(fa: Id[A]): Id[B] = I.ap(ff)(fa)

    override def map[A, B](fa: Id[A])(f: Id[A ⇒ B]): Id[B] = I.map(fa)(f)

    override def flatMap[A, B](fa: Id[A])(f: A ⇒ Id[B]): Id[B] = I.flatMap(fa)(f)

    //override def product[A, B](fa: Id[A], fb: Id[B]): Id[(A, B)] = I.product(fa, fb)

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
object IdInterpreters extends Interpreters[cats.Id] with IdInstances