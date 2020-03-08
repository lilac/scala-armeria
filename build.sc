import mill._
import scalalib._
import $ivy.`com.lihaoyi::mill-contrib-scalapblib:$MILL_VERSION`
import contrib.scalapblib._
import coursier.Repository
import coursier.maven.MavenRepository

val extraRepositories = Seq(
  MavenRepository("https://maven.aliyun.com/repository/public"),
  MavenRepository("https://maven.aliyun.com/repository/google")
)

trait Repo extends CoursierModule {
  def repositories: Seq[Repository] = extraRepositories
}

object CustomZincWorkerModule extends ZincWorkerModule {
  override def repositories = /*super.repositories ++ */extraRepositories
}

trait BaseModule extends JavaModule {
  override def zincWorker = CustomZincWorkerModule
}

trait BaseScalaModule extends ScalaModule with BaseModule {
  def scalaVersion = T { "2.12.4" }
}

object server extends BaseScalaModule with SbtModule {

  override def moduleDeps = Seq(protocol)

  override def ivyDeps = Agg(
    ivy"com.typesafe:config:1.4.0",
    ivy"com.linecorp.armeria:armeria-grpc:0.98.0",
    ivy"io.micrometer:micrometer-registry-elastic:1.3.5",
    ivy"org.apache.logging.log4j:log4j-slf4j-impl:2.9.1"
  )
}

object protocol extends ScalaPBModule with BaseScalaModule {

  def scalaPBVersion = "0.9.5"

  override def millSourcePath = super.millSourcePath / "src" / "main"

  import mill.scalalib.Lib.resolveDependencies
  import mill.api.Loose
  import mill.api.PathRef

  override def scalaPBClasspath: T[Loose.Agg[PathRef]] = T {
    resolveDependencies(
      Seq(
        coursier.LocalRepositories.ivy2Local
      ) ++ extraRepositories,
      Lib.depToDependency(_, "2.12.4"),
      Seq(ivy"com.thesamet.scalapb::scalapbc:${scalaPBVersion()}")
    )
  }
  // override def scalaPBFlatPackage: T[Boolean] = T { true }
  // override def scalaPBJavaConversions: T[Boolean] = T { true }
}

