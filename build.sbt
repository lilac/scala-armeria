import scala.concurrent.Await

import Dependencies._
import gigahorse.support.okhttp.Gigahorse

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

enablePlugins(ProtobufPlugin)

val PB = ProtobufPlugin

val logger: Logger = ConsoleLogger()

lazy val Versions = new {
  val gpb3Version = "3.11.1"
  val grpcVersion = "1.26.0"
}

def grpcExeFileName: String = {
  val os = if (scala.util.Properties.isMac) {
    "osx-x86_64"
  } else if (scala.util.Properties.isWin) {
    "windows-x86_64"
  } else {
    "linux-x86_64"
  }
  s"$grpcArtifactId-${Versions.grpcVersion}-$os.exe"
}
val grpcArtifactId = "protoc-gen-grpc-java"
val grpcExeUrl = s"https://maven.aliyun.com/repository/public/io/grpc/$grpcArtifactId/${Versions.grpcVersion}/$grpcExeFileName"
val grpcExePath = SettingKey[xsbti.api.Lazy[File]]("grpcExePath")
val grpcOutPath = SettingKey[File]("grpc out path")

val grpcSettings = Seq(
  grpcExePath := xsbti.api.SafeLazy.strict {
    val exe: File = baseDirectory.value / ".bin" / grpcExeFileName
    if (!exe.exists) {
      import scala.concurrent.duration._
      logger.info(s"gRPC protoc plugin (for Java) does not exist. Downloading from $grpcExeUrl")
      val req = Gigahorse.url(grpcExeUrl).withFollowRedirects(true)
      val f = Gigahorse.http(Gigahorse.config).download(req, exe)
      val file = Await.result(f, 120.second)
      logger.info(s"plugin saved to $file")
      exe.setExecutable(true)
    } else {
      logger.debug("gRPC protoc plugin (for Java) exists")
    }
    exe
  },
  grpcOutPath := (sourceManaged.value / "main" / "compiled_protobuf"),
  protobufProtocOptions in ProtobufConfig ++= Seq(
    s"--plugin=protoc-gen-java_rpc=${grpcExePath.value.get}",
    s"--java_rpc_out=${grpcOutPath.value.getAbsolutePath}"
  ),
)

lazy val root = (project in file("."))
  .settings(grpcSettings)
  .settings(
    name := "scala-armeria",
    libraryDependencies ++= Seq(
      armeria,
      armeriaGrpc
    ),
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
