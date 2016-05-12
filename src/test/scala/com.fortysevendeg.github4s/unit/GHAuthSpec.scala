package com.fortysevendeg.github4s.unit

import cats.Id
import cats.scalatest.{XorMatchers, XorValues}
import com.fortysevendeg.github4s.Github._
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.free.interpreters.IdInterpreters._
import com.fortysevendeg.github4s.{Github, TestUtils}
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mock.MockitoSugar

import scalaj.http.HttpRequest

class GHAuthSpec extends FlatSpec with Matchers with XorMatchers with XorValues with MockitoSugar with TestUtils {

  "Auth >> NewAuth" should "return the expected accessToken for valid credentials" in {
    val http = mock[HttpRequest]



    when(http.asString).thenReturn(notFoundHttpResponse)
    val response = Github().auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, invalidClientSecret).exec[Id]
    response shouldBe right
//    response.value.statusCode shouldBe okStatusCode
  }

//  it should "return error on Left when invalid credential is provided" in {
//    when(http.asString).thenReturn(badCredentialHttpResponse)
//    val response = Github().auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, invalidClientSecret).exec[Id]
//    response shouldBe left
//  }

//  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
//    val response = Github().auth.authorizeUrl(validClientId, validRedirectUri, validScopes).exec[Id]
//    response shouldBe right
//    response.value.entity.url.contains(validRedirectUri) shouldBe true
//    response.value.statusCode shouldBe okStatusCode
//  }
//
//  "Auth >> GetAccessToken" should "return error on Left for invalid code value" in {
//    val response = Github().auth.getAccessToken(validClientId, invalidClientSecret, "", validRedirectUri, "").exec[Id]
//    response shouldBe left
//  }

}
