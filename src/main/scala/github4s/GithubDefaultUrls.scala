package github4s

case class GithubApiUrls(
  baseUrl: String,
  authorizeUrl: String,
  accessTokenUrl: String
)

object GithubDefaultUrls {

  implicit val defaultUrls: GithubApiUrls = GithubApiUrls(
    "https://api.github.com/",
    "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
    "https://github.com/login/oauth/access_token"
  )
}