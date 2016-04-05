package com.fortysevendeg.github4s


import cats.data.Xor
import cats.syntax.xor._
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.GithubTypes.GHResponse
import io.circe._, io.circe.parser._
import scalaj.http._


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


  def get[A](method: String, data: Map[String, String] = Map.empty)(implicit C : GithubConfig, D : Decoder[A]): GHResponse[A] = {
    Http(buildURL(method))
        .headers(C.accessToken match {
          case Some(token) => Map("Authorization" -> s"token $token")
          case _ => Map.empty:Map[String, String]
        })
        .option(HttpOptions.connTimeout(connTimeoutMs))
        .option(HttpOptions.readTimeout(readTimeoutMs))
        .params(Map("per_page" -> "2"))
        .params(data)
        .asString match {
      case r if r.isSuccess => {

//        decode[A](r.body).fold(e => JsonParsingException(e.getMessage).left[A], _.right)

        decode[A](r.body).fold(e => JsonParsingException(e.getMessage).left[GHResult[A]], (result) => {
          result match {
            case Nil => Xor.Right(GHListResult(result, r.code, toLowerCase(r.headers), D))
            case _ :: _ => Xor.Right(GHListResult(result, r.code, toLowerCase(r.headers), D))
            case _ => Xor.Right(GHItemResult(result, r.code, toLowerCase(r.headers)))
          }
        })
      }
      case r => UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}").left[GHResult[A]]
    }

  }

  def getAPelo[A](url: String, d: Decoder[A])(implicit C : GithubConfig): GHResponse[A] = {
    Http(url)
        .headers(C.accessToken match {
          case Some(token) => Map("Authorization" -> s"token $token")
          case _ => Map.empty:Map[String, String]
        })
        .option(HttpOptions.connTimeout(connTimeoutMs))
        .option(HttpOptions.readTimeout(readTimeoutMs))
        .asString match {
      case r if r.isSuccess => {

        implicit val D: Decoder[A] = d
        decode[A](r.body).fold(e => JsonParsingException(e.getMessage).left[GHResult[A]], (result) => {
          result match {
            case Nil => Xor.Right(GHListResult(result, r.code, toLowerCase(r.headers), d))
            case _ :: _ => Xor.Right(GHListResult(result, r.code, toLowerCase(r.headers), d))
            case _ => Xor.Right(GHItemResult(result, r.code, toLowerCase(r.headers)))
          }
        })
      }
      case r => UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}").left[GHResult[A]]
    }

  }




  def toLowerCase(headers:Map[String, IndexedSeq[String]]): Map[String, IndexedSeq[String]] = headers.map(e=>(e._1.toLowerCase,e._2))


}
