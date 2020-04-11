package example

import scala.collection.JavaConverters._

import cats.effect._
import com.avast.grpc.jsonbridge.GrpcJsonBridge.GrpcMethodName
import com.avast.grpc.jsonbridge.scalapb.ScalaPBReflectionGrpcJsonBridge
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import io.grpc.protobuf.services.ProtoReflectionService
import org.slf4j.LoggerFactory

object Main extends App {
  private val logger = LoggerFactory.getLogger(getClass)
  val ec = ServiceModule.ec

  val service = GrpcService
    .builder()
    .addService(ServiceModule.helloServiceDefinition)
    .addService(ProtoReflectionService.newInstance())
    .supportedSerializationFormats(GrpcSerializationFormats.values())
    .enableUnframedRequests(true)
    .build()
  val port = 8000
  private val docService = DocService.builder()
    .build()
  val server = Server
    .builder()
    .http(port)
    .service(service, LoggingService.newDecorator())
    .serviceUnder("/docs", docService)
    .build()

  MetricsModule.initialize()
  Console.sshServer.start()

  System.out.println(s"Server starts at http://localhost:$port")
  val cf = server.start()

  private val bridge = ScalaPBReflectionGrpcJsonBridge.createFromServices[IO](ec)(service.services().asScala.toSeq: _*)
  val res = bridge.use { b =>
    b.invoke(GrpcMethodName("com.example.grpc.hello.HelloService/Hello"), """{"name": "abc"}""", Map())
  }.unsafeRunSync()
  println(res)

  cf.join()
}
