package example

import scala.concurrent.Future

import com.example.grpc.hello.hello.HelloServiceGrpc
import org.slf4j.LoggerFactory

/**
 * Copyright SameMo 2020
 */
object HelloService {
  private val logger = LoggerFactory.getLogger(getClass)
}

class HelloService extends HelloServiceGrpc.HelloService {
  import HelloService._
  import com.example.grpc.hello.hello._

  override def hello(request: HelloRequest): Future[HelloReply] = {
    val name = request.name
    logger.info("Received message: {}", name)
    val reply = HelloReply().withMessage(s"How are u, $name")
    Future.successful(reply)
  }

}