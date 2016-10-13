---
layout: home
technologies:
 - scala: ["Scala", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - android: ["Android", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
 - database: ["Database", "Lorem ipsum dolor sit amet, conse ctetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolo…"]
---

```tut:invisible
import org.scalatest._
import Matchers._
import cats.Eval
import cats.implicits._
import github4s.Github
import github4s.Github._
import github4s.implicits._

val accessToken = sys.props.get("token")
```

```tut:book
val user1 = Github(accessToken).users.get("rafaparadela").exec[Eval].value

user1 should be ('right)
user1.toOption map (_.result.login shouldBe "rafaparadela")
```

