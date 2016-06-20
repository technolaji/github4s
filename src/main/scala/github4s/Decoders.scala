package github4s

import github4s.free.domain._
import io.circe._, io.circe.generic.auto._, io.circe.jawn._, io.circe.syntax._
import cats.data.Xor

/** Implicit circe decoders of domains objects */
object Decoders {

  implicit val decodeCommit: Decoder[Commit] = Decoder.instance { c ⇒
    for {
      sha ← c.downField("sha").as[String]
      message ← c.downField("commit").downField("message").as[String]
      date ← c.downField("commit").downField("author").downField("date").as[String]
      url ← c.downField("html_url").as[String]
      login ← c.downField("author").downField("login").as[String]
      avatar_url ← c.downField("author").downField("avatar_url").as[String]
      author_url ← c.downField("author").downField("html_url").as[String]
    } yield Commit(sha, message, date, url, login, avatar_url, author_url)
  }

  implicit val decodeRepository: Decoder[Repository] = Decoder.instance { c ⇒
    for {
      id ← c.downField("id").as[Int]
      name ← c.downField("name").as[String]
      full_name ← c.downField("name").as[String]
      owner ← c.downField("owner").as[User]
      priv ← c.downField("private").as[Boolean]
      description ← c.downField("description").as[String]
      fork ← c.downField("fork").as[Boolean]
      created_at ← c.downField("created_at").as[String]
      updated_at ← c.downField("updated_at").as[String]
      pushed_at ← c.downField("pushed_at").as[String]
      homepage ← c.downField("homepage").as[Option[String]]
      language ← c.downField("language").as[Option[String]]
      organization ← c.downField("organization").as[Option[User]]
      size ← c.downField("size").as[Int]
      stargazers_count ← c.downField("stargazers_count").as[Int]
      watchers_count ← c.downField("watchers_count").as[Int]
      forks_count ← c.downField("forks_count").as[Int]
      open_issues_count ← c.downField("open_issues_count").as[Int]
      open_issues ← c.downField("open_issues").as[Int]
      watchers ← c.downField("watchers").as[Int]
      network_count ← c.downField("network_count").as[Int]
      subscribers_count ← c.downField("subscribers_count").as[Int]
      has_issues ← c.downField("has_issues").as[Boolean]
      has_downloads ← c.downField("has_downloads").as[Boolean]
      has_wiki ← c.downField("has_wiki").as[Boolean]
      has_pages ← c.downField("has_pages").as[Boolean]
      url ← c.downField("url").as[String]
      html_url ← c.downField("html_url").as[String]
      git_url ← c.downField("git_url").as[String]
      ssh_url ← c.downField("ssh_url").as[String]
      clone_url ← c.downField("clone_url").as[String]
      svn_url ← c.downField("svn_url").as[String]
    } yield Repository(
      id           = id,
      name         = name,
      full_name    = full_name,
      owner        = owner,
      `private`    = priv,
      description  = description,
      fork         = fork,
      created_at   = created_at,
      updated_at   = updated_at,
      pushed_at    = pushed_at,
      homepage     = homepage,
      language     = language,
      organization = organization,
      status       = RepoStatus(
        size              = size,
        stargazers_count  = stargazers_count,
        watchers_count    = watchers_count,
        forks_count       = forks_count,
        open_issues_count = open_issues_count,
        open_issues       = open_issues,
        watchers          = watchers,
        network_count     = network_count,
        subscribers_count = subscribers_count,
        has_issues        = has_issues,
        has_downloads     = has_downloads,
        has_wiki          = has_wiki,
        has_pages         = has_pages
      ),
      urls         = RepoUrls(
        url       = url,
        html_url  = html_url,
        git_url   = git_url,
        ssh_url   = ssh_url,
        clone_url = clone_url,
        svn_url   = svn_url
      )
    )
  }

}
