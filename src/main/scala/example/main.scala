package example

import com.example.grpc.hello.Hello
import com.example.grpc.hello.Hello.HelloReply
import com.example.grpc.hello.HelloServiceGrpc.HelloServiceImplBase
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory

object Main extends App {
  val service = GrpcService
    .builder()
    .addService(new HelloService())
    .addService(ProtoReflectionService.newInstance())
    .build()
  val server = Server
    .builder()
    .http(8000)
    .service(service, LoggingService.newDecorator())
    .serviceUnder("/docs", new DocService)
    .build()
  server.start().join()
}

object HelloService {
  private val logger = LoggerFactory.getLogger(getClass)
}

class HelloService extends HelloServiceImplBase {
  import HelloService._

  override def hello(
      request: Hello.HelloRequest,
      responseObserver: StreamObserver[Hello.HelloReply]): Unit = {
    val message = request.getName
    logger.info("Received message: {}", message)
    val reply = HelloReply.newBuilder().setMessage("hi").build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

}
