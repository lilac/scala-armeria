package example

import scala.concurrent.ExecutionContext

import com.example.grpc.hello.hello.HelloServiceGrpc

/**
 * Copyright SameMo 2020
 */
object ServiceModule {
  def ec = ExecutionContext.global
  def helloService = new HelloService()
  def helloServiceDefinition = HelloServiceGrpc.bindService(helloService, ec)
}
