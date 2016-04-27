package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.GithubResponses.GHResponse
import com.fortysevendeg.github4s.free.domain.Authorization
import com.fortysevendeg.github4s.HttpClient
import io.circe.generic.auto._
import io.circe.syntax._
import scalaj.http.HttpConstants._


object Auth {

  protected val httpClient = new HttpClient()

  case class NewAuthRequest(scopes: List[String], note: String, client_id: String, client_secret: String)

  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String): GHResponse[Authorization] =
    httpClient.postAuth[Authorization](
      method = "authorizations",
      headers = Map("Authorization" -> s"Basic ${base64(s"$username:$password")}"),
      data = NewAuthRequest(scopes, note, client_id, client_secret).asJson.noSpaces)

  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]): GHResponse[String] = ???


}
