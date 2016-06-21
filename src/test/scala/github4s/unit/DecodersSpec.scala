package github4s.unit

import cats.scalatest.{ XorValues, XorMatchers }
import github4s.free.domain.{ Commit, Repository }
import github4s.utils.FakeResponses
import github4s.Decoders._
import org.scalatest._
import io.circe.parser._

class DecodersSpec
  extends FlatSpec
  with Matchers
  with XorMatchers
  with XorValues
  with FakeResponses {

  "Commit decoder" should "return a list of commits when the JSON is valid" in {
    decode[List[Commit]](listCommitsValidResponse) shouldBe right
  }

  it should "return an empty list for an empty JSON" in {
    decode[List[Commit]](emptyListResponse).value.isEmpty shouldBe true
  }

  "Repository decoder" should "return a valid repo for a valid JSON" in {
    decode[Repository](getRepoResponse) shouldBe right
  }

  it should "return error for an empty JSON" in {
    decode[Repository](emptyListResponse) shouldBe left
  }

}
