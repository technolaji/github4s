import de.heikoseeberger.sbtheader.license.MIT
import catext.Dependencies._
import PgpKeys.gpgCommand

val dev  = Seq(Dev("47 Degrees (twitter: @47deg)", "47 Degrees"))
val gh   = GitHubSettings("com.fortysevendeg", "github4s", "47 Degrees", mit)
val vAll = Versions(versions, libraries, scalacPlugins)

pgpPassphrase := Some(sys.env.getOrElse("PGP_PASSPHRASE", "").toCharArray)
pgpPublicRing := file(s"${sys.env.getOrElse("PGP_FOLDER", ".")}/pubring.gpg")
pgpSecretRing := file(s"${sys.env.getOrElse("PGP_FOLDER", ".")}/secring.gpg")

lazy val buildSettings = Seq(
    name := gh.proj,
    organization := gh.org,
    organizationName := gh.publishOrg,
    description := "Github API wrapper written in Scala",
    startYear := Option(2016),
    homepage := Option(url("http://47deg.github.io/github4s/")),
    organizationHomepage := Option(new URL("http://47deg.com")),
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.10.6", scalaVersion.value, "2.12.1"),
    scalacOptions ++= (scalaBinaryVersion.value match {
      case "2.10" => Seq("-Xdivergence211")
      case _      => Nil
    }),
    scalafmtConfig in ThisBuild := Some(file(".scalafmt")),
    headers := Map(
      "scala" -> MIT("2016", "47 Degrees, LLC. <http://www.47deg.com>")
    )
  ) ++ reformatOnCompileSettings ++
    sharedCommonSettings ++
    miscSettings ++
    sharedReleaseProcess ++
    credentialSettings ++
    sharedPublishSettings(gh, dev)

lazy val micrositeSettings = Seq(
  micrositeName := "github4s",
  micrositeDescription := "Github API wrapper written in Scala",
  micrositeBaseUrl := "github4s",
  micrositeDocumentationUrl := "/github4s/docs.html",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "github4s",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
)

lazy val commonDeps = addLibs(vAll,
                                "cats-free",
                                "circe-core",
                                "circe-generic",
                                "circe-parser",
                                "simulacrum") ++
    addCompilerPlugins(vAll, "paradise") ++
Seq(libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.0.0" % "test",
  "com.github.marklister" %%% "base64" % "0.2.3"
))

lazy val jvmDeps = Seq(
      libraryDependencies ++= Seq(
        "org.scalaj" %% "scalaj-http" % "2.2.1",
        "org.mock-server" % "mockserver-netty" % "3.10.4" % "test"
      ))

lazy val jsDeps = Seq(
  libraryDependencies ++= Seq(
    "fr.hmil" %%% "roshttp" % "2.0.0-RC1"
  )
)

lazy val docsDependencies = libraryDependencies ++= Seq(
    "com.ironcorelabs" %% "cats-scalatest"  % "1.1.2"  % "test",
    "org.mock-server"  % "mockserver-netty" % "3.10.4" % "test"
  )

lazy val scalazDependencies = addLibs(vAll, "scalaz-concurrent")

/** github4s - cross project that provides cross platform support.*/
lazy val github4s = (crossProject in file("github4s"))
  .settings(moduleName := "github4s")
  .enablePlugins(AutomateHeaderPlugin)
    .enablePlugins(BuildInfoPlugin).
    settings(
      buildInfoKeys := Seq[BuildInfoKey](name, version, "token" -> Option(sys.props("token")).getOrElse("")),
      buildInfoPackage := "github4s"
    )
  .settings(buildSettings: _*)
  .settings(commonDeps: _*)
  .jvmSettings(jvmDeps: _*)
  .jsSettings(sharedJsSettings: _*)
  .jsSettings(testSettings: _*)
  .jsSettings(jsDeps: _*)

lazy val github4sJVM = github4s.jvm
lazy val github4sJS  = github4s.js

lazy val docs = (project in file("docs"))
  .dependsOn(scalaz)
  .settings(moduleName := "github4s-docs")
  .settings(buildSettings: _*)
  .settings(micrositeSettings: _*)
  .settings(docsDependencies: _*)
  .settings(noPublishSettings: _*)
  .enablePlugins(MicrositesPlugin)

lazy val scalaz = (project in file("scalaz"))
  .settings(moduleName := "github4s-scalaz")
  .settings(buildSettings: _*)
  .settings(scalazDependencies: _*)
  .dependsOn(github4sJVM)
  .enablePlugins(AutomateHeaderPlugin)

lazy val testSettings = Seq(
  fork in Test := false
)
