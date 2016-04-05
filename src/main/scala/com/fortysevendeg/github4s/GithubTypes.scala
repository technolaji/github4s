package com.fortysevendeg.github4s

import cats.data.Xor
import cats.free.Free
import com.fortysevendeg.github4s.GithubResponses.{GHResult, GHException}
import com.fortysevendeg.github4s.app._

object GithubTypes {

  type GHIO[A] = Free[GitHub4s, A]

  type GHResponse[A] = GHException Xor GHResult[A]
}
