organization := "com.fortysevendeg"

name := "github4s"

version := "0.2-SNAPSHOT"

scalaVersion := "2.11.8"

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