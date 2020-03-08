
import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

resolvers += "aliyun public" at "https://maven.aliyun.com/repository/public"
resolvers += "google" at "https://maven-central.storage.googleapis.com/maven2"
// externalResolvers += "aliyun central" at "http://maven.aliyun.com/repository/central"
// externalResolvers += "aliyun jcenter" at "http://maven.aliyun.com/repository/jcenter"
externalResolvers := Resolver.combineDefaultResolvers(resolvers.value.toVector, false)

lazy val root = (project in file("."))
  .enablePlugins(GrpcPlugin)
  .settings(
    name := "scala-armeria",
    libraryDependencies ++= Seq(
      armeria,
      armeriaGrpc,
      microMeter,
      "com.typesafe" % "config" % "1.4.0"
    ),
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
