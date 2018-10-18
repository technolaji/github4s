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

package github4s.effect

import cats._
import cats.data._
import cats.tagless._
import cats.effect._
import cats.syntax.all._
import io.circe.Decoder
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.dsl.io._
import org.http4s.blaze._
import org.http4s.client.blaze._
import github4s._
import github4s.Decoders._
import github4s.free.domain._
import github4s.free.interpreters._
import java.util.concurrent._
import scala.concurrent.ExecutionContext

object tagless {

  object HTTP {
    implicit def withClient[F[_] : ConcurrentEffect, A](
      useClient: Client[F] => F[A]
      )(
      implicit EC: ExecutionContext,
               CS: ContextShift[F]
      ): F[A] =
        BlazeClientBuilder[F](EC).resource.use(c => useClient(c))
  }

  @finalAlg
  @autoFunctorK
  @autoSemigroupalK
  @autoProductNK
  trait Repositories[F[_]] {
    def get(owner: String, repo: String): F[Repository]
  }

  implicit def repositories[F[_] : ConcurrentEffect](
      implicit EC: ExecutionContext,
               CS: ContextShift[F]): Repositories[F] =
    new Repositories[F]{
      def get(owner: String, repo: String): F[Repository] = {
        val uri = Uri.uri("https://api.github.com/") / "repos" / owner / repo
        HTTP.withClient[F, Repository] { (c: Client[F]) =>
          c.expect(uri)(jsonOf[F, Repository])
        }
      }
    }

  @finalAlg
  @autoFunctorK
  @autoSemigroupalK
  @autoProductNK
  trait Issues[F[_]] {
    def get(owner: String, repo: String): F[List[Issue]]
  }

  implicit def issues[F[_] : ConcurrentEffect](
    implicit EC: ExecutionContext,
             CS: ContextShift[F]): Issues[F] =
    new Issues[F]{
      def get(owner: String, repo: String): F[List[Issue]] = {
        val uri = Uri.uri("https://api.github.com/") / "repos" / owner / repo / "issues"
        HTTP.withClient[F, List[Issue]] { (c: Client[F]) =>
          c.expect(uri)(jsonOf[F, List[Issue]])
        }
      }
        Sync[F].pure(List.empty)
    }


  def program[F[_] : Sync](owner: String, repo: String)(
    implicit R: Repositories[F],
             I: Issues[F]
    ): F[(Repository, List[Issue])] = 
      (R.get(owner, repo), I.get(owner, repo)).tupled
}



trait SyncCaptureInstance {
  implicit def syncCaptureInstance[F[_]: Sync] = new Capture[F] {
    override def capture[A](a: ⇒ A): F[A] = Sync[F].delay(a)
  }
}
