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
import github4s.api.Users
import github4s.free.adt.UserOp._
import github4s.free.domain._
import github4s.free.domain.User._

/**
 * Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object UserOps {

  @free trait UserOpsM {

    def getUser(username: String, accessToken: Option[String] = None): FS[GHResponse[User]]

    def getAuthUser(accessToken: Option[String] = None): FS[GHResponse[User]]

    def getUsers(
        since: Int,
        pagination: Option[Pagination] = None,
        accessToken: Option[String] = None): FS[GHResponse[List[User]]]

  }

  trait Implicits {

    /**
     * Lifts User Ops to an effect capturing Monad such as Task via natural transformations
     */
    def userOpsInterpreter: UserOp ~> K =
      new (UserOp ~> K) {

        val users = new Users()

        def apply[A](fa: UserOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case GetUser(username, accessToken) ⇒ users.get(accessToken, headers, username)
            case GetAuthUser(accessToken)       ⇒ users.getAuth(accessToken, headers)
            case GetUsers(since, pagination, accessToken) ⇒
              users.getUsers(accessToken, headers, since, pagination)
          }
        }
      }

  }

  object implicits extends Implicits
}
