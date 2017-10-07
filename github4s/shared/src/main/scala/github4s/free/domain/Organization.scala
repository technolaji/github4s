/**
 * https://github.com/AdrianRaFo
 */
package github4s.free.domain

class Organization {

  final case class ListMembers(
      org: String,
      filter: Option[String] = None,
      role: Option[String] = None,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None
  )
}
