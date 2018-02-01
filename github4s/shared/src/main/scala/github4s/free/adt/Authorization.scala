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

package github4s.free.adt

import github4s.GithubResponses.GHResponse
import github4s.free.domain.{Authorization, Authorize, OAuthToken}

/**
 * Auths ops ADT
 */
sealed trait AuthOp[A]

final case class NewAuth(
    username: String,
    password: String,
    scopes: List[String],
    note: String,
    client_id: String,
    client_secret: String
) extends AuthOp[GHResponse[Authorization]]

final case class AuthorizeUrl(
    client_id: String,
    redirect_uri: String,
    scopes: List[String]
) extends AuthOp[GHResponse[Authorize]]

final case class GetAccessToken(
    client_id: String,
    client_secret: String,
    code: String,
    redirect_uri: String,
    state: String
) extends AuthOp[GHResponse[OAuthToken]]
