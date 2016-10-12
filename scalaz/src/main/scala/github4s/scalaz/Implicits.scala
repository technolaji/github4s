package github4s.scalaz

import cats.{ MonadError, FlatMap }
import scalaz.concurrent.Task

object implicits {

  implicit def g4sTaskMonadError(implicit FM: FlatMap[Task]): MonadError[Task, Throwable] = new MonadError[Task, Throwable] {

    override def pure[A](x: A): Task[A] = Task.now(x)

    override def map[A, B](fa: Task[A])(f: A ⇒ B): Task[B] = fa.map(f)

    override def flatMap[A, B](fa: Task[A])(ff: A ⇒ Task[B]): Task[B] = fa.flatMap(ff)

    override def tailRecM[A, B](a: A)(f: A ⇒ Task[Either[A, B]]): Task[B] = FM.tailRecM(a)(f)

    override def raiseError[A](e: Throwable): Task[A] = Task.fail(e)

    override def handleErrorWith[A](fa: Task[A])(f: Throwable ⇒ Task[A]): Task[A] = fa.handleWith({ case x ⇒ f(x) })
  }

}
