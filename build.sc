import mill._, scalalib._
import $ivy.`com.lihaoyi::mill-contrib-scalapblib:$MILL_VERSION`

import contrib.scalapblib._
import coursier.maven.MavenRepository

val extraRepositories = Seq(
  MavenRepository("https://maven.aliyun.com/repository/public"),
  MavenRepository("https://maven.aliyun.com/repository/google")
)

trait Repo extends CoursierModule {
  def repositories = extraRepositories
}

object CustomZincWorkerModule extends ZincWorkerModule {
  def repositories = /*super.repositories ++ */extraRepositories
}

trait BaseModule extends JavaModule {
  def zincWorker = CustomZincWorkerModule
}

object root extends SbtModule with BaseModule {
  def scalaVersion = "2.12.4"

  def millSourcePath = build.millSourcePath

  object protobuf extends ScalaPBModule with BaseModule {
    def scalaVersion = root.scalaVersion

    def scalaPBVersion = "0.9.5"

    def millSourcePath = root.millSourcePath / "src" / "main"

    import mill.scalalib.Lib.resolveDependencies
    import mill.api.Loose
    import mill.api.PathRef

    def scalaPBClasspath: T[Loose.Agg[PathRef]] = T {
      resolveDependencies(
        Seq(
          coursier.LocalRepositories.ivy2Local,
        ) ++ extraRepositories,
        Lib.depToDependency(_, "2.12.4"),
        Seq(ivy"com.thesamet.scalapb::scalapbc:${scalaPBVersion()}")
      )
    }
    // override def scalaPBFlatPackage: T[Boolean] = T { true }
    // override def scalaPBJavaConversions: T[Boolean] = T { true }
  }

  def moduleDeps = Seq(protobuf)

  def ivyDeps = Agg(
    ivy"com.typesafe:config:1.4.0",
    ivy"com.linecorp.armeria:armeria-grpc:0.98.0",
    ivy"io.micrometer:micrometer-registry-elastic:1.3.5"
  )
}

