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

package github4s.free.domain

sealed trait SearchParam {
  protected def paramName: String
  protected def paramValue: String
  def value: String = s"$paramName:$paramValue"
}

sealed trait IssueType extends SearchParam {
  override def paramName: String = "type"
}

case object IssueTypeIssue extends IssueType {
  override def paramValue: String = "issue"
}

case object IssueTypePullRequest extends IssueType {
  override def paramValue: String = "pr"
}

case class SearchIn(values: Set[SearchInValue]) extends SearchParam {
  override def paramName: String  = "in"
  override def paramValue: String = values.map(_.value).mkString(",")
}

sealed trait SearchInValue {
  def value: String
}

case object SearchInTitle extends SearchInValue {
  override def value: String = "title"
}

case object SearchInBody extends SearchInValue {
  override def value: String = "body"
}

case object SearchInComments extends SearchInValue {
  override def value: String = "comments"
}

sealed trait IssueState extends SearchParam {
  override def paramName: String = "state"
}

case object IssueStateOpen extends IssueState {
  override def paramValue: String = "open"
}

case object IssueStateClosed extends IssueState {
  override def paramValue: String = "closed"
}

case class LabelParam(label: String, exclude: Boolean = false) extends SearchParam {
  override def paramName: String = s"${if (exclude) "-" else ""}state"

  override def paramValue: String = label
}

sealed trait OwnerParam extends SearchParam

case class OwnerParamOwnedByUser(user: String) extends OwnerParam {
  override def paramName: String = "user"

  override def paramValue: String = user
}

case class OwnerParamInRepository(repo: String) extends OwnerParam {
  override def paramName: String = "repo"

  override def paramValue: String = repo
}
