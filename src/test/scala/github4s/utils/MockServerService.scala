package github4s.utils

import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.integration.ClientAndServer._
import org.scalatest.{ BeforeAndAfterAll, FlatSpec }

trait MockServerService extends FlatSpec with BeforeAndAfterAll {

  val mockServerPort = 9999

  val mockServer = startClientAndServer(mockServerPort)

  override def beforeAll = ConfigurationProperties.overrideLogLevel("OFF")

  override def afterAll = mockServer.stop

}
