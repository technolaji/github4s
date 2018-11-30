package github4s.unit


import cats.free.Free
import github4s.{GHRepos, GHWebhooks}
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.app.GitHub4s
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHWebhookSpec extends BaseSpec{

  "GHWebhooks.listWebhooks" should "call to WebhookOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[List[Webhook]]] =
      Free.pure(Right(GHResult(List(webhook), okStatusCode, Map.empty)))

    val webhookOps = mock[WebhookOpsTest]
    (webhookOps.listWebhooks _)
      .expects(sampleToken, validRepoOwner, validRepoName)
      .returns(response)
    val ghWebhookData = new GHWebhooks(sampleToken)(webhookOps)
    ghWebhookData.listWebhooks(validRepoOwner, validRepoName)
  }



}
