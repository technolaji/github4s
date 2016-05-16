package com.fortysevendeg.github4s.free.domain

case class Authorization(
  id: Int,
  url: String,
  token: String
)

case class NewAuthRequest(
  scopes: List[String],
  note: String,
  client_id: String,
  client_secret: String
)

case class Authorize(
  url: String,
  state: String
)

case class OAuthToken(
  access_token: String,
  token_type: String,
  scope: String
)

case class NewOAuthRequest(
  client_id: String,
  client_secret: String,
  code: String,
  redirect_uri: String,
  state: String
)