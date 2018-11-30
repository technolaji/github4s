package github4s.integration

import github4s.Github
import github4s.Github._
import github4s.free.domain._
import github4s.implicits._
import github4s.utils.BaseIntegrationSpec


trait GHWebhooksSpec[T] extends BaseIntegrationSpec[T]{

  "Webhooks >> ListHooks" should "return an empty or non empty list when a valid ref and owner are provided" in {
    val response = Github(accessToken).webhooks
      .listWebhooks(validRepoOwner, validRepoName)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Webhook]](response, { r =>
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid org or owner are provided" in {
    val response = Github(accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, invalidRef)
      .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }




}
