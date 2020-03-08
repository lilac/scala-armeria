package example

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import io.grpc.protobuf.services.ProtoReflectionService
import io.micrometer.elastic.ElasticMeterRegistry
import org.slf4j.LoggerFactory

object Main extends App {
  private val logger = LoggerFactory.getLogger(getClass)
  val service = GrpcService
    .builder()
    .addService(ServiceModule.helloServiceDefinition)
    .addService(ProtoReflectionService.newInstance())
    .supportedSerializationFormats(GrpcSerializationFormats.values())
    .enableUnframedRequests(true)
    .build()
  val port = 8000
  val server = Server
    .builder()
    .http(port)
    .service(service, LoggingService.newDecorator())
    .serviceUnder("/docs", new DocService)
    .build()

  import io.micrometer.core.instrument.Clock

  val elasticConfig = new ElasticConfig()
  val registry = new ElasticMeterRegistry(elasticConfig, Clock.SYSTEM)

  System.out.println(s"Server starts at http://localhost:$port")
  server.start().join()
}
