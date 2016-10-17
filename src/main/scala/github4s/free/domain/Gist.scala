package github4s.free.domain

case class Gist(
  url: String,
  id: String,
  description: String,
  public: Boolean
)

case class GistFile(
  content: String
)

case class NewGistRequest(
  description: String,
  public: Boolean,
  files: Map[String, GistFile]
)