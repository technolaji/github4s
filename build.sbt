import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

organization := "com.fortysevendeg"

name := "github4s"

version := "0.2-SNAPSHOT"

scalaVersion := "2.11.8"


SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(SpacesWithinPatternBinders, true)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpacesAroundMultiImports,true)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(CompactStringConcatenation,false)
    .setPreference(DanglingCloseParenthesis,Force)
    .setPreference(CompactControlReadability, false)
    .setPreference(AlignParameters, false)
    .setPreference(AlignArguments, true)
    .setPreference(AlignSingleLineCaseStatements, false)
    .setPreference(DoubleIndentClassDeclaration, false)
    .setPreference(IndentLocalDefs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
    .setPreference(RewriteArrowSymbols, true)

enablePlugins(SiteScaladocPlugin)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.4.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.4",
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "io.circe" %% "circe-core" % "0.3.0",
  "io.circe" %% "circe-generic" % "0.3.0",
  "io.circe" %% "circe-parser" % "0.3.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.ironcorelabs" %% "cats-scalatest" % "1.1.2" % "test"
)

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))