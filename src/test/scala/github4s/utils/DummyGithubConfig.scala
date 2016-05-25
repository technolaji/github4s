package github4s.utils

import github4s.GithubApiConfig

trait DummyGithubConfig {

  val dummyConfigHocon =
    """
      |github.baseUrl = "http://127.0.0.1:9999/"
      |github.authorizeUrl = "http://127.0.0.1:9999/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"
      |github.accessTokenUrl = "http://127.0.0.1:9999/login/oauth/access_token"
      |""".stripMargin

  implicit val dummyConfig = new GithubApiConfig(Option(dummyConfigHocon))
}
