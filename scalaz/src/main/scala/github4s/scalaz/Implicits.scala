package github4s.scalaz

import cats.implicits._
import github4s.Github._
import cats.{ MonadError, RecursiveTailRecM }
import scalaz.concurrent.Task
import github4s.free.interpreters.Capture
import scalaz._

object implicits {

  implicit val taskCaptureInstance = new Capture[Task] {
    override def capture[A](a: ⇒ A): Task[A] = Task.now(a)
  }

  implicit val g4sTaskcatsRecursiveTailRecM: RecursiveTailRecM[Task] = new RecursiveTailRecM[Task] {}

  implicit def g4sTaskMonadError: MonadError[Task, Throwable] = new MonadError[Task, Throwable] {

    override def pure[A](x: A): Task[A] = Task.now(x)

    override def map[A, B](fa: Task[A])(f: A ⇒ B): Task[B] = fa.map(f)

    override def flatMap[A, B](fa: Task[A])(ff: A ⇒ Task[B]): Task[B] = fa.flatMap(ff)

    override def tailRecM[A, B](a: A)(f: A ⇒ Task[Either[A, B]]): Task[B] =
      Task.tailrecM(a)(A ⇒ f(a) map (t ⇒ toScalazDisjunction(t)))

    override def raiseError[A](e: Throwable): Task[A] = Task.fail(e)

    override def handleErrorWith[A](fa: Task[A])(f: Throwable ⇒ Task[A]): Task[A] = fa.handleWith({ case x ⇒ f(x) })

  }

  private[this] def toScalazDisjunction[A, B](disj: Either[A, B]): A \/ B =
    disj.fold(l ⇒ -\/(l), r ⇒ \/-(r))
}
