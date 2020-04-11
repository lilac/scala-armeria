
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

lazy val protocol = (project in file("protocol"))
  .enablePlugins(GrpcPlugin)
  .settings(
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-protobuf" % "1.27.0",
      "io.grpc" % "grpc-stub" % "1.27.0",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    )
  )
lazy val root = (project in file("server"))
  .settings(
    name := "scala-armeria",
    libraryDependencies ++= Seq(
      armeria,
      armeriaGrpc,
      microMeter,
      "com.typesafe" % "config" % "1.4.0",
      "com.lihaoyi" % "ammonite-sshd_2.13.1" % "2.0.4",
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.9.1",
      "com.avast.grpc" %% "grpc-json-bridge-core-scalapb" % "0.17.5",
      "com.avast.grpc" %% "grpc-json-bridge-core" % "0.17.5"
    ),
    libraryDependencies += scalaTest % Test,
    externalResolvers := externalResolvers.value // Resolver.combineDefaultResolvers(resolvers.value.toVector, false)
  ).dependsOn(protocol)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
