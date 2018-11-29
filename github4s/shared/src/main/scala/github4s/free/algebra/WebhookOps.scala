/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.free.algebra

import cats.InjectK
import cats.free.Free
import github4s.GithubResponses.GHResponse
import github4s.free.domain._

/**
  * Webhook ops ADT
  */

sealed trait WebhookOp[A]

final case class ListWebhooks(
                               accessToken: Option[String] = None,
                               owner: String,
                               repo: String
                             ) extends WebhookOp[GHResponse[List[Webhook]]]


/**
  * Exposes Webhook operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */

class WebhookOps[F[_]](implicit I: InjectK[WebhookOp, F]) {

  def listWebhooks(
                    accessToken: Option[String] = None,
                    owner: String,
                    repo: String
                  ): Free[F, GHResponse[List[Webhook]]] = Free.inject[WebhookOp, F](ListWebhooks(accessToken, owner, repo))

}

object WebhookOps {
  implicit def instance[F[_]](implicit I: InjectK[WebhookOp, F]): WebhookOps[F] = new WebhookOps[F]
}