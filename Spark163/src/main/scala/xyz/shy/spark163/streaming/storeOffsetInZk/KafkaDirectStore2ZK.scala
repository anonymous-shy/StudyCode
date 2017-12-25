package xyz.shy.spark163.streaming.storeOffsetInZk

import java.util.Arrays

import com.typesafe.config.ConfigFactory
import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo}
import kafka.common.{BrokerNotAvailableException, TopicAndPartition}
import kafka.consumer.SimpleConsumer
import kafka.utils.{Json, ZKGroupTopicDirs, ZKStringSerializer, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.immutable

/**
  * Created by Shy on 2017/12/25
  */

object KafkaDirectStore2ZK {

  def main(args: Array[String]): Unit = {
    val resConf = ConfigFactory.load()
    val confTopic: String = resConf.getString("KafkaTopics")
    var topics = Seq[String]()
    confTopic.split(",").foreach(topics += _)
    val group = resConf.getString("KafkaGroup")
    val zkQuorums = resConf.getString("com.ZKNodes")
    val brokerList = resConf.getString("com.KafkaBrokers")
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "zookeeper.connect" -> zkQuorums,
      "auto.offset.reset" -> kafka.api.OffsetRequest.SmallestTimeString
    )
    val sparkConf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    //创建zkClient注意最后一个参数最好是ZKStringSerializer类型的，不然写进去zk里面的偏移量是乱码
    val zkClient = new ZkClient(zkQuorums, 30000, 30000, ZKStringSerializer)
    val partitions4Topics = ZkUtils.getPartitionsForTopics(zkClient, topics)
    var fromOffsets: Map[TopicAndPartition, Long] = Map()
    partitions4Topics.foreach(partitions4Topic => {
      val topic = partitions4Topic._1
      val partitions = partitions4Topic._2
      /**
        * kafka.utils.ZkUtils.ZKGroupTopicDirs
        * val ConsumersPath = "/consumers"
        * val BrokerIdsPath = "/brokers/ids"
        * val BrokerTopicsPath = "/brokers/topics"
        * val TopicConfigPath = "/config/topics"
        * val TopicConfigChangesPath = "/config/changes"
        * val ControllerPath = "/controller"
        * val ControllerEpochPath = "/controller_epoch"
        * val ReassignPartitionsPath = "/admin/reassign_partitions"
        * val DeleteTopicsPath = "/admin/delete_topics"
        * val PreferredReplicaLeaderElectionPath = "/admin/preferred_replica_election"
        * *
        * class ZKGroupDirs(val group: String) {
        * def consumerDir = ZkUtils.ConsumersPath
        * def consumerGroupDir = consumerDir + "/" + group
        * def consumerRegistryDir = consumerGroupDir + "/ids"
        * }
        *
        * class ZKGroupTopicDirs(group: String, topic: String) extends ZKGroupDirs(group) {
        * def consumerOffsetDir = consumerGroupDir + "/offsets/" + topic
        * def consumerOwnerDir = consumerGroupDir + "/owners/" + topic
        * }
        */
      val topicDirs = new ZKGroupTopicDirs(group, topic)
      partitions.foreach(partition => {
        //获取 zookeeper 中的路径，这里会变成 /consumers/$group/offsets/$topic  <=> /consumers/group-shy/offsets/topic1
        val zkPath = s"${topicDirs.consumerOffsetDir}/$partition"
        /**
          *
          * make sure a persistent path exists in ZK. Create the path if not exist.
          * 若为第一次则创建zkPath
          *
          * def makeSurePersistentPathExists(client: ZkClient, path: String) {
          * if (!client.exists(path))
          *     client.createPersistent(path, true) // won't throw NoNodeException or NodeExistsException
          * }
          */
        ZkUtils.makeSurePersistentPathExists(zkClient, zkPath)
        val untilOffset = zkClient.readData[String](zkPath)
        val tp = TopicAndPartition(topic, partition)
      })
    })
  }

  private def getMinOffset(zkClient: ZkClient, tp: TopicAndPartition): Long = {
    val request = OffsetRequest(immutable.Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))

    ZkUtils.getLeaderForPartition(zkClient, tp.topic, tp.partition) match {
      case Some(brokerId) => {
        ZkUtils.readDataMaybeNull(zkClient, ZkUtils.BrokerIdsPath + "/" + brokerId)._1 match {
          case Some(brokerInfoString) => {
            Json.parseFull(brokerInfoString) match {
              case Some(m) =>
                val brokerInfo = m.asInstanceOf[Map[String, Any]]
                val host = brokerInfo("host").asInstanceOf[String]
                val port = brokerInfo("port").asInstanceOf[Int]
                new SimpleConsumer(host, port, 10000, 100000, "getMinOffset")
                  .getOffsetsBefore(request)
                  .partitionErrorAndOffsets(tp)
                  .offsets
                  .head
              case None =>
                throw new BrokerNotAvailableException("Broker id %d does not exist".format(brokerId))
            }
          }
          case None =>
            throw new BrokerNotAvailableException("Broker id %d does not exist".format(brokerId))
        }
      }
      case None =>
        throw new Exception("No broker for partition %s - %s".format(tp.topic, tp.partition))
    }
  }
}
