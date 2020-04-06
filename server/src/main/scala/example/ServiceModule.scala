package example

import scala.concurrent.ExecutionContext

/**
 * Copyright SameMo 2020
 */
object ServiceModule {
  def ec = ExecutionContext.global
  def helloService = new HelloService()
  def helloServiceDefinition = helloService
}
