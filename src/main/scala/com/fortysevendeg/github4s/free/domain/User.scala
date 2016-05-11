package com.fortysevendeg.github4s.free.domain

case class User(
    id: Int,
    login: String,
    avatar_url: String,
    html_url: String,
    name: Option[String] = None,
    email: Option[String] = None)
