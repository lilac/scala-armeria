package example

import com.example.grpc.hello.Hello.{ HelloReply, HelloRequest }
import com.example.grpc.hello.HelloServiceGrpc.HelloServiceImplBase
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory

/**
 * Copyright SameMo 2020
 */
object PBHelloService {
  private val logger = LoggerFactory.getLogger(getClass)
}

class PBHelloService extends HelloServiceImplBase {
  import PBHelloService._

  /**
   */
  override def hello(request: HelloRequest, responseObserver: StreamObserver[HelloReply]): Unit = {
    val name = request.getName
    logger.info("Received message: {}", name)
    val reply = HelloReply.newBuilder().setMessage(s"How are u, $name").build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

}