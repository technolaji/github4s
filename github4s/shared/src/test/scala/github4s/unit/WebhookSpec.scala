package github4s.unit

import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.api.Webhooks
import github4s.free.domain.Webhook
import github4s.utils.BaseSpec
import cats.Id
import github4s.HttpClient


class WebhookSpec extends BaseSpec {

  "Webhooks.listHooks" should "call htppClient.get with the right parameters" in {
    val response: GHResponse[List[Webhook]] = Right(GHResult(List(webhook), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Webhook]](
      url = s"repos/$validRepoOwner/$validRepoName/hooks",
      response = response
    )

    val webhooks = new Webhooks[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    webhooks.listWebhooks(sampleToken, headerUserAgent, validRepoOwner, validRepoName)

  }
}
