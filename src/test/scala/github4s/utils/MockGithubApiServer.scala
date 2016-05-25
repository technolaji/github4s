package github4s.utils

import org.mockserver.model.HttpRequest._
import org.mockserver.model.HttpResponse._
import org.mockserver.model.NottableString._
import org.mockserver.model.{ Parameter, ParameterBody }
import org.mockserver.model.JsonBody._

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
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  //Auth >> new auth
  mockServer.when(request.withMethod("POST").withPath(s"/authorizations").withHeader("Authorization", validBasicAuth))
    .respond(response.withStatusCode(okStatusCode).withBody(newAuthValidResponse))

  mockServer.when(request.withMethod("POST").withPath(s"/authorizations").withHeader("Authorization", invalidBasicAuth))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(badCredentialsResponse))

  //Auth >> get access token
  mockServer.when(request.withMethod("POST").withPath(s"/login/oauth/access_token")
    .withBody(json(s"{client_id:'',client_secret:'',code:'$validCode',redirect_uri:'',state:''}")))
    .respond(response.withStatusCode(okStatusCode).withBody(getAccessTokenValidResponse))

  mockServer.when(request.withMethod("POST").withPath(s"/login/oauth/access_token")
    .withBody(json(s"{client_id:'',client_secret:'',code:'$invalidCode',redirect_uri:'',state:''}")))
    .respond(response.withStatusCode(okStatusCode).withBody(badVerificationResponse))

  //Repos >> get repo
  mockServer.when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$validRepoName"))
    .respond(response.withStatusCode(okStatusCode).withBody(getRepoResponse))

  mockServer.when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$invalidRepoName"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Repos >> list commits
  mockServer.when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$validRepoName/commits")
    .withQueryStringParameter("page", validPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(listCommitsValidResponse))

  mockServer.when(request.withMethod("GET")
    .withPath(s"/repos/$validRepoOwner/$validRepoName/commits")
    .withQueryStringParameter("page", invalidPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer.when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$invalidRepoName/commits"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

}