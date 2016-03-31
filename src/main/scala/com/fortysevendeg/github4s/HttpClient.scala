package com.fortysevendeg.github4s

import cats.data.Xor
import cats.syntax.xor._
import io.circe._, io.circe.parser._
import scalaj.http._

sealed abstract class HttpException(msg : String) extends Exception(msg)
final case class GHException(msg : String) extends HttpException(msg)

class HttpClient(baseURL: String = "https://api.github.com") {

  private val connTimeoutMs: Int = 1000
  private val readTimeoutMs: Int = 5000

  private def buildURL(method: String) = s"$baseURL/$method"

//  def post(method: String, data: Map[String, String]) = Http(buildURL(method))
//        .postForm
//        .option(HttpOptions.connTimeout(connTimeoutMs))
//        .option(HttpOptions.readTimeout(readTimeoutMs))
//        .params(data)
//        .asString


  def get[A : Decoder](method: String, data: Map[String, String] = Map.empty): Xor[Exception, A] = {
    Http(buildURL(method))
        .option(HttpOptions.connTimeout(connTimeoutMs))
        .option(HttpOptions.readTimeout(readTimeoutMs))
        .params(data)
        .asString match {
      case r if r.isSuccess => decode[A](r.body)
      case r => GHException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}").left[A]
    }

  }


}
