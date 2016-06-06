package github4s.utils

import github4s.GithubApiUrls

trait DummyGithubUrls {

  implicit val dummyUrls: GithubApiUrls = GithubApiUrls(
    baseUrl        = "http://127.0.0.1:9999/",
    authorizeUrl   = "http://127.0.0.1:9999/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
    accessTokenUrl = "http://127.0.0.1:9999/login/oauth/access_token"
  )
}
