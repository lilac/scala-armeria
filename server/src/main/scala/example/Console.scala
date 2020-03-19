package example

import ammonite.sshd.{ SshServerConfig, SshdRepl }
import ammonite.util.Bind
import org.apache.sshd.server.config.keys.DefaultAuthorizedKeysAuthenticator

object Console {

  private val prelude =
    """
import scala.concurrent._
import scala.concurrent.duration._

implicit class ExtendedFuture[A](val f: Future[A]) {
  def await: A = Await.result(f, Duration.Inf)
}
"""
  val config = SshServerConfig(
    address = "localhost",
    port = 22222,
    publicKeyAuthenticator = Some(new DefaultAuthorizedKeysAuthenticator(false))
  )
  val sshServer = new SshdRepl(
    config,
    predef = prelude,
    replArgs = List(
      Bind("main", Main),
    )
  )
}