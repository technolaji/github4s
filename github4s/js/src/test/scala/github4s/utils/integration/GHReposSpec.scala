/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s.integration

import cats.Id
import cats.implicits._
import github4s.Github._
import github4s.GithubResponses._
import github4s.implicits._
import github4s.{Github, ImplicitsJS}
import github4s.utils.TestUtils
import org.scalatest.{AsyncFlatSpec, FlatSpec, Matchers}
import fr.hmil.roshttp.HttpResponse
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GHReposSpec extends AsyncFlatSpec with Matchers with TestUtils with ImplicitsJS {

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response =
      Github(accessToken).repos.get(validRepoOwner, validRepoName).exec[Future, HttpResponse]
    response should be('right)

    response map { r ⇒
      r.toOption map { rr =>
        rr.result.name shouldBe validRepoName
        rr.statusCode shouldBe okStatusCode
      } match {
        case _ => succeed
      }
    }
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).repos.get(validRepoOwner, invalidRepoName).exec[Future, HttpResponse]
    response should be('left)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response =
      Github(accessToken).repos
        .listCommits(validRepoOwner, validRepoName)
        .exec[Future, HttpResponse]
    response should be('right)

    response map { r ⇒
      r.toOption map { rr =>
        rr.result.nonEmpty shouldBe true
        rr.statusCode shouldBe okStatusCode
      } match {
        case _ => succeed
      }
    }
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listCommits(invalidRepoName, validRepoName)
        .exec[Future, HttpResponse]
    response should be('left)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response =
      Github(accessToken).repos
        .listContributors(validRepoOwner, validRepoName)
        .exec[Future, HttpResponse]
    response should be('right)

    response map { r ⇒
      r.toOption map { rr =>
        rr.result shouldNot be(empty)
        rr.statusCode shouldBe okStatusCode
      } match {
        case _ => succeed
      }
    }
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listContributors(invalidRepoName, validRepoName)
        .exec[Future, HttpResponse]
    response should be('left)
  }

}
