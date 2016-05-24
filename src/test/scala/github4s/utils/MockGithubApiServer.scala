package github4s.utils

import org.mockserver.model.HttpRequest._
import org.mockserver.model.HttpResponse._
import org.mockserver.model.NottableString._

trait MockGithubApiServer extends MockServerService with FakeResponses with TestUtils {

  //Users >> get
  mockServer.when(request.withMethod("GET").withPath(s"/users/$validUsername"))
    .respond(response.withStatusCode(okStatusCode).withBody(getUserValidResponse))

  mockServer.when(request.withMethod("GET").withPath(s"/users/$invalidUsername"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Users >> auth
  mockServer.when(request.withMethod("GET").withPath(s"/user").withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(getUserValidResponse))

  mockServer.when(request.withMethod("GET").withPath(s"/user").withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedReponse))

  //Users >> get users
  mockServer.when(request.withMethod("GET").withPath(s"/users").withQueryStringParameter("since", validSinceInt.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(getUsersValidResponse))

  mockServer.when(request.withMethod("GET").withPath(s"/users").withQueryStringParameter("since", invalidSinceInt.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(getUsersEmptyResponse))

}