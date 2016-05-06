package com.fortysevendeg.github4s

import com.fortysevendeg.github4s.free.domain.Commit
import io.circe._, io.circe.generic.auto._, io.circe.jawn._, io.circe.syntax._
import cats.data.Xor

object Decoders {

  implicit val decodeCommit: Decoder[Commit] = Decoder.instance { c =>
    for {
      sha <- c.downField("sha").as[String]
      message <- c.downField("commit").downField("message").as[String]
      date <- c.downField("commit").downField("author").downField("date").as[String]
      url <- c.downField("html_url").as[String]
      login <- c.downField("author").downField("login").as[String]
      avatar_url <- c.downField("author").downField("avatar_url").as[String]
      author_url <- c.downField("author").downField("html_url").as[String]
    } yield Commit(sha, message, date, url, login, avatar_url, author_url)
  }


}
