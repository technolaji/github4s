package github4s.utils

trait FakeResponses {

  val getUserValidResponse =
    """
      |{
      |  "login": "rafaparadela",
      |  "id": 315070,
      |  "avatar_url": "https://avatars.githubusercontent.com/u/315070?v=3",
      |  "gravatar_id": "",
      |  "url": "https://api.github.com/users/rafaparadela",
      |  "html_url": "https://github.com/rafaparadela",
      |  "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |  "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |  "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |  "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |  "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |  "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |  "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |  "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |  "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |  "type": "User",
      |  "site_admin": false,
      |  "name": "Rafa Paradela",
      |  "company": "47 Degrees",
      |  "blog": "http://rafaparadela.github.io",
      |  "location": "Cádiz, Spain",
      |  "email": "rafa.p@47deg.com",
      |  "hireable": null,
      |  "bio": "Hola",
      |  "public_repos": 36,
      |  "public_gists": 13,
      |  "followers": 41,
      |  "following": 32,
      |  "created_at": "2010-06-26T07:59:52Z",
      |  "updated_at": "2016-05-17T15:43:17Z"
      |}
    """.stripMargin

  val getUsersValidResponse =
    """
      |[
      |  {
      |    "login": "aslakhellesoy",
      |    "id": 1000,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/1000?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/aslakhellesoy",
      |    "html_url": "https://github.com/aslakhellesoy",
      |    "followers_url": "https://api.github.com/users/aslakhellesoy/followers",
      |    "following_url": "https://api.github.com/users/aslakhellesoy/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/aslakhellesoy/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/aslakhellesoy/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/aslakhellesoy/subscriptions",
      |    "organizations_url": "https://api.github.com/users/aslakhellesoy/orgs",
      |    "repos_url": "https://api.github.com/users/aslakhellesoy/repos",
      |    "events_url": "https://api.github.com/users/aslakhellesoy/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/aslakhellesoy/received_events",
      |    "type": "User",
      |    "site_admin": false
      |  }
      | ]
    """.stripMargin

  val getUsersEmptyResponse =
    """
      |[]
    """.stripMargin

  val notFoundResponse =
    """
      |{
      |  "message": "Not Found",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

  val unauthorizedReponse =
    """
      |{
      |  "message": "Requires authentication",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

}
