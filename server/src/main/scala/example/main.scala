package example

import scala.concurrent.{ ExecutionContext, Future }

import com.example.grpc.hello.hello.HelloServiceGrpc
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import io.grpc.protobuf.services.ProtoReflectionService
import io.micrometer.elastic.ElasticMeterRegistry
import org.slf4j.LoggerFactory

object Main extends App {
  val service = GrpcService
    .builder()
    .addService(ServiceModule.helloServiceDefinition)
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

object ServiceModule {
  def ec = ExecutionContext.global
  def helloService = new HelloService()
  def helloServiceDefinition = HelloServiceGrpc.bindService(helloService, ec)
}

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
