package com.fortysevendeg.github4s.api

import java.util.UUID

import cats.data.Xor
import com.fortysevendeg.github4s.GithubResponses.{GHItemResult, GHResponse}
import com.fortysevendeg.github4s.free.domain._
import com.fortysevendeg.github4s.HttpClient
import io.circe.generic.auto._
import io.circe.syntax._
import scalaj.http.HttpConstants._


object Auth {

  protected val httpClient = new HttpClient()

  val authorizeUrl = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"

  /**
    * Call to request a new authorization given a basic authentication, the returned object Authorization includes an
    * access token
    * @param username
    * @param password
    * @param scopes
    * @param note
    * @param client_id
    * @param client_secret
    * @return
    */
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


  /**
    * Generates the authorize url with a random state, both are returned within Authorize object
    *
    * @param client_id
    * @param redirect_uri
    * @param scopes
    * @return
    */
  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]): GHResponse[Authorize] = {
    val state = UUID.randomUUID().toString
    Xor.Right(
      GHItemResult(
        value = Authorize(authorizeUrl.format(client_id, redirect_uri, scopes.mkString(","), state), state),
        statusCode = 200,
        headers = Map.empty))
  }


  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String): GHResponse[OAuthToken] = ???
  )


}
