package example

import io.micrometer.core.instrument.binder.jvm.{ ClassLoaderMetrics, ExecutorServiceMetrics, JvmGcMetrics, JvmMemoryMetrics, JvmThreadMetrics }
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.{ Clock, Metrics }
import io.micrometer.elastic.ElasticMeterRegistry

/**
 * Copyright SameMo 2020
 */
object MetricsModule {
  lazy val elasticConfig = new ElasticConfig()
  lazy val registry = new ElasticMeterRegistry(elasticConfig, Clock.SYSTEM)
  lazy val metrics = Seq(
    new ProcessorMetrics(),
    new JvmThreadMetrics(),
    new JvmGcMetrics(),
    new JvmMemoryMetrics(),
    new ClassLoaderMetrics(),
  )

  def initialize(): Unit = {
    Metrics.addRegistry(registry)
    for (metric <- metrics) {
      metric.bindTo(registry)
    }
  }
}
