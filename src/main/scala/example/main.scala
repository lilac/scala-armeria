package example

import com.example.grpc.hello.Hello
import com.example.grpc.hello.Hello.HelloReply
import com.example.grpc.hello.HelloServiceGrpc.HelloServiceImplBase
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import io.micrometer.elastic.ElasticMeterRegistry

object Main extends App {
  val service = GrpcService
    .builder()
    .addService(new HelloService())
    .addService(ProtoReflectionService.newInstance())
    .supportedSerializationFormats(GrpcSerializationFormats.values())
    .enableUnframedRequests(true)
    .build()
  val server = Server
    .builder()
    .http(8000)
    .service(service, LoggingService.newDecorator())
    .serviceUnder("/docs", new DocService)
    .build()

  import io.micrometer.core.instrument.Clock

  val elasticConfig = new ElasticConfig()
  val registry = new ElasticMeterRegistry(elasticConfig, Clock.SYSTEM)
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
    val name = request.getName
    logger.info("Received message: {}", name)
    val reply = HelloReply.newBuilder().setMessage(s"How are u, $name").build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

}
