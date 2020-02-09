import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

enablePlugins(ProtobufPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "scala-armeria",
    libraryDependencies ++= Seq(
      armeria,
      armeriaGrpc
    ),
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
