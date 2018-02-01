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
import freestyle.free.{free, FreeS, FreeSLift}
import github4s.GithubResponses._
import github4s.api.Activities
import github4s.free.adt.ActivityOp._
import github4s.free.domain._
import github4s.free.domain.Activity._

/**
 * Exposes Activity operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object ActivityOps {

  @free trait ActivityOpsM {

    def setThreadSub(
        id: Int,
        subscribed: Boolean,
        ignored: Boolean,
        accessToken: Option[String] = None): FS[GHResponse[Subscription]]

    def listStargazers(
        owner: String,
        repo: String,
        timeline: Boolean,
        pagination: Option[Pagination] = None,
        accessToken: Option[String] = None): FS[GHResponse[List[Stargazer]]]

    def listStarredRepositories(
        username: String,
        timeline: Boolean,
        sort: Option[String] = None,
        direction: Option[String] = None,
        pagination: Option[Pagination] = None,
        accessToken: Option[String] = None): FS[GHResponse[List[StarredRepository]]]
  }

  trait Implicits {

    /**
     * Lifts Activity Ops to an effect capturing Monad such as Task via natural transformations
     */
    def activityOpsInterpreter: ActivityOp ~> K =
      new (ActivityOp ~> K) {

        val activities = new Activities()

        def apply[A](fa: ActivityOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case SetThreadSub(id, subscribed, ignored, accessToken) ⇒
              activities.setThreadSub(accessToken, headers, id, subscribed, ignored)
            case ListStargazers(owner, repo, timeline, pagination, accessToken) ⇒
              activities.listStargazers(accessToken, headers, owner, repo, timeline, pagination)
            case ListStarredRepositories(
                username,
                timeline,
                sort,
                direction,
                pagination,
                accessToken) ⇒
              activities.listStarredRepositories(
                accessToken,
                headers,
                username,
                timeline,
                sort,
                direction,
                pagination)
          }
        }
      }
  }

  object implicits extends Implicits
}
