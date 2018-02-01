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
import github4s.api.Gists
import github4s.free.adt.GistOp._
import github4s.free.domain.Gist._

/**
 * Exposes Gists operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object GistOps {

  @free trait GistOpsM {
    def newGist(
        description: String,
        public: Boolean,
        files: Map[String, GistFile],
        accessToken: Option[String] = None
    ): FS[GHResponse[Gist]]
  }

  trait Implicits {

    /**
     * Lifts Gist Ops to an effect capturing Monad such as Task via natural transformations
     */
    def gistOpsInterpreter: GistOp ~> K =
      new (GistOp ~> K) {

        val gists = new Gists()

        def apply[A](fa: GistOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
          fa match {
            case NewGist(description, public, files, accessToken) ⇒
              gists.newGist(description, public, files, headers, accessToken)
          }
        }
      }

  }

  object implicits extends Implicits
}
