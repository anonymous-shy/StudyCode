package xyz.shy.spark220.utils

import com.typesafe.config.{Config, ConfigFactory}
import kafka.zk.KafkaZkClient
import org.apache.kafka.common.utils.Time
import org.apache.log4j.Logger

object KafkaZKUtils {

  lazy private val LOG: Logger = Logger.getLogger(this.getClass)

  val resConf: Config = ConfigFactory.load()
  val GROUP_ID: String = resConf.getString("com.KafkaGroup")
  val BOOTSTRAP: String = resConf.getString("com.KafkaBrokers")
  val TOPICS: String = resConf.getString("com.KafkaTopics")
  val zkQuorums: String = resConf.getString("com.ZKNodes")

  /**
    * 返回示例:
    * Map(T1 -> ArrayBuffer(0, 1, 2, 3, 4, 5), T2 -> ArrayBuffer(0, 1, 2, 3, 4, 5))
    *
    * @param topic topic
    * @return Map[String, Seq[Int]
    */
  def getKafkaTopicAPartitions(topic: String): Map[String, Seq[Int]] = {
    val zookeeperHost = zkQuorums
    val isSucre = false
    val sessionTimeoutMs = 60000
    val connectionTimeoutMs = 30000
    val maxInFlightRequests = 10
    val time = Time.SYSTEM
    val metricGroup = "kafka.server"
    val metricType = "ZooKeeperClientMetrics"
    val kafkaZkClient = KafkaZkClient(zookeeperHost,
      isSucre,
      sessionTimeoutMs,
      connectionTimeoutMs,
      maxInFlightRequests,
      time)
    val partitions4Topics: Map[String, Seq[Int]] = kafkaZkClient.getPartitionsForTopics(Set(topic))
    partitions4Topics
  }
}
