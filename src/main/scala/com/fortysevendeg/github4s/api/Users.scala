package com.fortysevendeg.github4s.api

import com.fortysevendeg.github4s.HttpClient
import com.fortysevendeg.github4s.free.domain.Collaborator


object Users {

  import io.circe.generic.auto._

  protected val httpClient = new HttpClient()

  def get(username: String): Option[Collaborator] = httpClient.get[Collaborator](s"users/$username").toOption

}
