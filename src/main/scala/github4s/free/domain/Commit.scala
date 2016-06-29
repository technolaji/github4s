package github4s.free.domain

case class Commit(
  sha: String,
  message: String,
  date: String,
  url: String,
  login: Option[String],
  avatar_url: Option[String],
  author_url: Option[String]
)
