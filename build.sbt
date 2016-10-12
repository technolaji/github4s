import com.typesafe.sbt.SbtGhPages.ghpages
import com.typesafe.sbt.SbtGit.git

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys


lazy val buildSettings = Seq(
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  description := "Github API wrapper written in Scala",
  startYear := Option(2016),
  homepage := Option(url("http://47deg.github.io/github4s/")),
  organizationHomepage := Option(new URL("http://47deg.com")),
  scalaVersion := "2.11.8",
  licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
)

lazy val cats = "0.7.2"
lazy val circe = "0.5.2"

lazy val dependencies = libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-free" % cats,
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "io.circe" %% "circe-core" % circe,
  "io.circe" %% "circe-generic" % circe,
  "io.circe" %% "circe-parser" % circe,
  "com.github.mpilquist" %% "simulacrum" % "0.8.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.ironcorelabs" %% "cats-scalatest" % "1.1.2" % "test",
  "org.mock-server" % "mockserver-netty" % "3.10.4" % "test",
  compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val scalariformSettings = SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(SpacesWithinPatternBinders, true)
      .setPreference(SpaceBeforeColon, false)
      .setPreference(SpaceInsideParentheses, false)
      .setPreference(SpaceInsideBrackets, false)
      .setPreference(SpacesAroundMultiImports, true)
      .setPreference(PreserveSpaceBeforeArguments, false)
      .setPreference(CompactStringConcatenation, false)
      .setPreference(DanglingCloseParenthesis, Force)
      .setPreference(CompactControlReadability, false)
      .setPreference(AlignParameters, false)
      .setPreference(AlignArguments, true)
      .setPreference(AlignSingleLineCaseStatements, false)
      .setPreference(DoubleIndentClassDeclaration, false)
      .setPreference(IndentLocalDefs, false)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
      .setPreference(RewriteArrowSymbols, true))


lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false)

lazy val tutDirectoriesSettings = Seq(
  tutSourceDirectory := sourceDirectory.value / "tut",
  tutTargetDirectory := sourceDirectory.value / "jekyll"
)

lazy val github4sSettings = buildSettings ++ dependencies ++ scalariformSettings

lazy val ghpagesSettings = ghpages.settings ++ Seq(git.remoteRepo := "git@github.com:47deg/github4s.git")

lazy val docsSettings = buildSettings ++ docsDependencies ++ noPublishSettings ++ tutSettings ++ tutDirectoriesSettings ++ ghpagesSettings

lazy val docsDependencies = libraryDependencies ++= Seq(
  "com.ironcorelabs" %% "cats-scalatest" % "1.1.2" % "test",
  "org.mock-server" % "mockserver-netty" % "3.10.4" % "test"
)

lazy val github4s = (project in file("."))
    .settings(moduleName := "github4s")
    .settings(github4sSettings:_*)

lazy val docs = (project in file("docs"))
    .settings(moduleName := "github4s-docs")
    .settings(docsSettings: _*)
    .enablePlugins(JekyllPlugin)
    .dependsOn(scalaz)

lazy val scalazDependencies = libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-concurrent" % "7.3.0-M5"
)

lazy val scalazSettings = buildSettings ++ scalazDependencies ++ scalariformSettings

lazy val scalaz = (project in file("scalaz"))
    .settings(moduleName := "github4s-scalaz")
    .settings(scalazSettings: _*)
    .dependsOn(github4s)
