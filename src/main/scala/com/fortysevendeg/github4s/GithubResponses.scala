package com.fortysevendeg.github4s

import cats.free.Free
import com.fortysevendeg.github4s.GithubTypes.GHResponse
import com.fortysevendeg.github4s.app.GitHub4s
import com.fortysevendeg.github4s.free.algebra.RequestOps
import io.circe.Decoder


object GithubResponses {


  sealed trait GHResult[A]

  final case class GHItemResult[A](value: A, statusCode: Int, headers: Map[String, IndexedSeq[String]]) extends GHResult[A]

  final case class GHListResult[A](value: A, statusCode: Int, headers: Map[String, IndexedSeq[String]], decoder: Decoder[A]) extends GHResult[A]


  sealed abstract class GHException(msg : String, cause : Option[Throwable] = None) extends Exception(msg) {
    cause foreach initCause
  }

  case class JsonParsingException(msg : String) extends GHException(msg)

  case class UnexpectedException(msg : String) extends GHException(msg)

}