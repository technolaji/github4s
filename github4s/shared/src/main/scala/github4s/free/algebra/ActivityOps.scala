/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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
import github4s.GithubResponses._
import github4s.free.domain._

/**
 * Exposes Activity operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
@free trait ActivityOps {

  def setThreadSub(id: Int, subscribed: Boolean, ignored: Boolean): FS[GHResponse[Subscription]]

  def listStargazers(
      owner: String,
      repo: String,
      timeline: Boolean,
      pagination: Option[Pagination] = None
  ): FS[GHResponse[List[Stargazer]]]

  def listStarredRepositories(
      username: String,
      timeline: Boolean,
      sort: Option[String] = None,
      direction: Option[String] = None,
      pagination: Option[Pagination] = None
  ): FS[GHResponse[List[StarredRepository]]]
}
