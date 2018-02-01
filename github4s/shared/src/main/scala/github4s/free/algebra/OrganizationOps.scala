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
import github4s.api.Organizations
import github4s.free.adt.OrganizationOp._
import github4s.free.domain._
import github4s.free.domain.User._

/**
 * Exposes Organization operations as a Free monadic algebra that may be combined with other
 * Algebras via Coproduct
 */
object OrganizationOps {

  @free trait OrganizationOpsM {

    def listMembers(
        org: String,
        filter: Option[String] = None,
        role: Option[String] = None,
        pagination: Option[Pagination] = None,
        accessToken: Option[String] = None): FS[GHResponse[List[User]]]
  }

  trait Implicits {

    /**
     * Lifts Organization Ops to an effect capturing Monad such as Task via natural transformations
     */
    def organizationOpsInterpreter: OrganizationOp ~> K =
      new (OrganizationOp ~> K) {

        val organizations = new Organizations()

        def apply[A](fa: OrganizationOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case ListMembers(org, filter, role, pagination, accessToken) ⇒
              organizations.listMembers(accessToken, headers, org, filter, role, pagination)
          }
        }
      }

  }

  object implicits extends Implicits
}
