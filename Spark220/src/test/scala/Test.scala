import kafka.zk.KafkaZkClient
import org.apache.kafka.common.utils.Time

object Test extends App {

  val zookeeperHost = "tagtic-slave01:12181"
  val isSucre = false
  val sessionTimeoutMs = 60000
  val connectionTimeoutMs = 30000
  val maxInFlightRequests = 10
  val time = Time.SYSTEM
  val metricGroup = "myGroup"
  val metricType = "myType"
  val kafkaZkClient = KafkaZkClient(zookeeperHost,
    isSucre,
    sessionTimeoutMs,
    connectionTimeoutMs,
    maxInFlightRequests,
    time)

  val partitions4Topics: Map[String, Seq[Int]] = kafkaZkClient.getPartitionsForTopics(Set("T1", "T2"))
  print(partitions4Topics.toString)
}
