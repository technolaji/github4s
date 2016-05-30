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
import cats.scalatest.XorMatchers.right
import cats.scalatest.XorValues
import cats.scalatest.XorValues._
import cats.Eval
import github4s.Github
import github4s.Github._
import github4s.Implicits._
import github4s.Implicits.EvalInterpreters._
```

```tut:book
val user1 = Github().users.get("rafaparadela").exec[Eval].value

user1 shouldBe right
user1.value.result.login shouldBe "rafaparadela"
```

