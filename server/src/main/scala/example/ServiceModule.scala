package example

import scala.concurrent.ExecutionContext

import com.example.grpc.hello.hello.HelloServiceGrpc
import io.grpc.ServerServiceDefinition

/**
 * Copyright SameMo 2020
 */
object ServiceModule {
  def ec = ExecutionContext.global
  def helloService = new HelloService
  def PBHelloService = new PBHelloService
  def helloServiceDefinition: ServerServiceDefinition = HelloServiceGrpc.bindService(helloService, ec)
}
