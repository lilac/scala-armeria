package example

import scala.util.control.NonFatal

import org.slf4j.LoggerFactory
import io.micrometer.elastic
import com.typesafe.config.ConfigFactory

/**
 * Copyright SameMo 2020
 */
class ElasticConfig extends elastic.ElasticConfig {
  private val logger = LoggerFactory.getLogger(getClass)
  private val config = ConfigFactory.load
    .getConfig("management.metrics.export")

  override def get(key: String): String = {
    try {
      config.getString(key)
    } catch {
      case NonFatal(e) =>
        // logger.warn(s"Config missing for key: $key")
        null
    }
  }

}
