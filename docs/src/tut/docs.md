---
layout: docs
---

# Get started

WIP: Import

```tut:silent
import github4s.Github
```

```tut:invisible
val accessToken = sys.props.get("token")
```

WIP: Every Github4s api returns a `Free[GHResponse[A], A]` where `GHResonse[A]` is a type alias for `Either[GHException, GHResult[A]]`. GHResult contains the result `[A]` given by Github, but also the status code of the response and headers:

```scala
case class GHResult[A](result: A, statusCode: Int, headers: Map[String, IndexedSeq[String]])
```

For geting an user

```tut:silent
val user1 = Github(accessToken).users.get("rafaparadela")
```

user1 in this case `Free[GHException Xor GHResult[User], User]` and we can run (`foldMap`) with `exec[M[_]]` where `M[_]` represent any type container that implements `MonadError[M, Throwable]`, for instance `cats.Eval`.

```tut:silent
import cats.Eval
import github4s.Github._
import github4s.implicits._

val u1 = user1.exec[Eval].value
```

WIP: As mentioned above `u1` should have an `GHResult[User]` in the right.

```tut:invisible
import cats.implicits._
import github4s.GithubResponses.GHResult
```

```tut:book
u1 match {
  case Right(GHResult(result, status, headers)) => result.login
  case Left(e) => e.getMessage
}
```

WIP:  With `Id`

```tut:silent
import cats.Id

val u2 = Github(accessToken).users.get("raulraja").exec[Id]
```

WIP: With `Future`

```tut:silent
import github4s.implicits._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Await

val u3 = Github(accessToken).users.get("dialelo").exec[Future]
Await.result(u3, 2.seconds)
```

WIP: With `scalaz.Task`

```tut:silent
import scalaz.concurrent.Task
import github4s.scalaz.implicits._

val u4 = Github(accessToken).users.get("franciscodr").exec[Task]
u4.attemptRun
```

