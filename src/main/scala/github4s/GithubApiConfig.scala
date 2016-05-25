package github4s

import com.typesafe.config.ConfigFactory

class GithubApiConfig(hocon: Option[String] = None) {

  val config = hocon.fold(ConfigFactory.load)(ConfigFactory.parseString)

  def getString(key: String) = sys.props.getOrElse(key, config.getString(key))
}

object GithubApiConfig {

  implicit val defaultConfig: GithubApiConfig = new GithubApiConfig
}