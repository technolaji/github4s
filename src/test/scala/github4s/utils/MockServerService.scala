package github4s.utils

import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.integration.ClientAndServer._
import org.mockserver.model.Header
import org.scalatest.{ BeforeAndAfterAll, FlatSpec }

trait MockServerService extends FlatSpec with BeforeAndAfterAll {

  val jsonHeader = new Header("Content-Type", "application/json; charset=utf-8")
  val mockServerPort = 9999

  lazy val mockServer = startClientAndServer(mockServerPort)

  override def beforeAll = {
    ConfigurationProperties.overrideLogLevel("ERROR")
    mockServer
  }

  override def afterAll = mockServer.stop

}
