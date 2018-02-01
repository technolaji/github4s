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
import github4s.free.domain.{Pagination, User}

/**
 * Users ops ADT
 */
sealed trait UserOp[A]

final case class GetUser(username: String, accessToken: Option[String] = None)
    extends UserOp[GHResponse[User]]
final case class GetAuthUser(accessToken: Option[String] = None) extends UserOp[GHResponse[User]]
final case class GetUsers(
    since: Int,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None)
    extends UserOp[GHResponse[List[User]]]
