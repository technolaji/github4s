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

import cats.~>
import freestyle.free._
import github4s.GithubResponses._
import github4s.api.Auth
import github4s.free.adt.AuthorizationOp._
import github4s.free.domain.Authorization._

/**
 * Exposes Auths operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object AuthorizationOps {

  @free trait AuthorizationOpsM {

    def newAuth(
        username: String,
        password: String,
        scopes: List[String],
        note: String,
        client_id: String,
        client_secret: String
    ): FS[GHResponse[Authorization]]

    def authorizeUrl(
        client_id: String,
        redirect_uri: String,
        scopes: List[String]
    ): FS[GHResponse[Authorize]]

    def getAccessToken(
        client_id: String,
        client_secret: String,
        code: String,
        redirect_uri: String,
        state: String
    ): FS[GHResponse[OAuthToken]]

  }

  trait Implicits {

    /**
     * Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
     */
    def AuthorizationOpsInterpreter: AuthorizationOp ~> K =
      new (AuthorizationOp ~> K) {

        val auth = new Auth()

        def apply[A](fa: AuthorizationOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒
              auth.newAuth(username, password, scopes, note, client_id, client_secret, headers)
            case AuthorizeUrl(client_id, redirect_uri, scopes) ⇒
              auth.authorizeUrl(client_id, redirect_uri, scopes)
            case GetAccessToken(client_id, client_secret, code, redirect_uri, state) ⇒
              auth.getAccessToken(client_id, client_secret, code, redirect_uri, state, headers)
          }
        }
      }

  }

  object implicits extends Implicits
}
