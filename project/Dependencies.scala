import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  lazy val armeria = "com.linecorp.armeria" % "armeria" % "0.98.0"
  lazy val armeriaGrpc = "com.linecorp.armeria" % "armeria-grpc" % "0.98.0"
}
