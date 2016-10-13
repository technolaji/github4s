package github4s.unit

import github4s.free.domain.{ Commit, Repository }
import github4s.utils.FakeResponses
import github4s.Decoders._
import org.scalatest._
import io.circe.parser._
import cats.implicits._

class DecodersSpec
  extends FlatSpec
  with Matchers
  with FakeResponses {

  "Commit decoder" should "return a list of commits when the JSON is valid" in {
    decode[List[Commit]](listCommitsValidResponse) should be('right)
  }

  it should "return an empty list for an empty JSON" in {
    decode[List[Commit]](emptyListResponse).toOption map (_.isEmpty shouldBe true)
  }

  "Repository decoder" should "return a valid repo for a valid JSON" in {
    decode[Repository](getRepoResponse) should be('right)
  }

  it should "return error for an empty JSON" in {
    decode[Repository](emptyListResponse) should be('left)
  }

}
