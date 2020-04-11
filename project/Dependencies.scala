import sbt._

object Dependencies {
  object Version {
    val armeria = "0.99.1"
  }
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  lazy val armeria = "com.linecorp.armeria" % "armeria" % Version.armeria
  lazy val armeriaGrpc = "com.linecorp.armeria" % "armeria-grpc" % Version.armeria
  lazy val microMeter = "io.micrometer" % "micrometer-registry-elastic" % "1.3.5"
}
