package github4s.scalaz

import cats.MonadError
import scalaz.concurrent.Task

object implicits {

  implicit val g4sTaskMonadError: MonadError[Task, Throwable] = new MonadError[Task, Throwable] {

    override def pure[A](x: A): Task[A] = Task.now(x)

    override def map[A, B](fa: Task[A])(f: A ⇒ B): Task[B] = fa.map(f)

    override def flatMap[A, B](fa: Task[A])(ff: A ⇒ Task[B]): Task[B] = fa.flatMap(ff)

    override def raiseError[A](e: Throwable): Task[A] = Task.fail(e)

    override def handleErrorWith[A](fa: Task[A])(f: Throwable ⇒ Task[A]): Task[A] = fa.handleWith({ case x ⇒ f(x) })
  }

}
