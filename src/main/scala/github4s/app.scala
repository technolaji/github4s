package github4s

import cats.data.Coproduct
import github4s.free.algebra._

object app {
  type COGH01[A] = Coproduct[RepositoryOp, UserOp, A]
  type GitHub4s[A] = Coproduct[AuthOp, COGH01, A]
}