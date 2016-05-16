package com.fortysevendeg.github4s.free.domain

case class Commit(
  sha: String,
  message: String,
  date: String,
  url: String,
  login: String,
  avatar_url: String,
  author_url: String
)