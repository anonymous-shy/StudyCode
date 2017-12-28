package xyz.shy.spark163.utils

import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo}
import kafka.common.{BrokerNotAvailableException, TopicAndPartition}
import kafka.consumer.SimpleConsumer
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{Json, ZKGroupTopicDirs, ZKStringSerializer, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{KafkaUtils, OffsetRange}
import org.slf4j.{Logger, LoggerFactory}


/**
  * Created by Shy on 2017/12/28
  */

object StreamingUtils {
  lazy private val log: Logger = LoggerFactory.getLogger(getClass)

  def CreateKafkaDirectStream(ssc: StreamingContext,
                              kafkaParams: Map[String, String],
                              zkServers: String,
                              topics: Seq[String]): InputDStream[(String, String)] = {
    var kps: Map[String, String] = kafkaParams
    val kafkaGroup: String = kps("group.id")
    //创建zkClient注意最后一个参数最好是ZKStringSerializer类型的，不然写进去zk里面的偏移量是乱码
    val zkClient = new ZkClient(zkServers, 30000, 30000, ZKStringSerializer)
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
      val topicDirs = new ZKGroupTopicDirs(kafkaGroup, topic)
      partitions.foreach(partition => {
        //获取 zookeeper 中的路径，这里会变成 /consumers/$group/offsets/$topic  <=> /consumers/group-shy/offsets/topic1
        val zkPath = s"${topicDirs.consumerOffsetDir}/$partition"
        /**
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
        val offset = try {
          if (untilOffset == null || untilOffset.trim.equals("")) {
            kps += ("auto.offset.reset" -> OffsetRequest.SmallestTimeString)
            getMinOffset(zkClient, tp)
          } else
            untilOffset.toLong
        } catch {
          case e: Exception => getMinOffset(zkClient, tp)
        }
        fromOffsets += (tp -> offset)
        log.info(s"@@@@@@ topic[ $topic ] partition[ $partition ] offset[ $offset ] @@@@@@")
      })
    })
    //这个会将 kafka 的消息进行 transform，最终 kafka 的数据都会变成 (topic_name, message) 这样的 tuple
    val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message())
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    kafkaStream
  }

  def saveOffsets(offsetRanges: Array[OffsetRange], kafkaParams: Map[String, String], zkServers: String): Unit = {
    val kafkaGroup: String = kafkaParams("group.id")
    val zkClient = new ZkClient(zkServers, 30000, 30000, ZKStringSerializer)
    offsetRanges.foreach(o => {
      val topicDirs = new ZKGroupTopicDirs(kafkaGroup, o.topic)
      val zkPath = s"${topicDirs.consumerOffsetDir}/${o.partition}"
      ZkUtils.updatePersistentPath(zkClient, zkPath, o.untilOffset.toString)
      log.info(s"@@@@@@ ${o.toString()} @@@@@@")
      // log.info(s"Offset update: set offset of ${o.topic}/${o.partition} as ${o.untilOffset.toString}")
    })
  }

  private def getMinOffset(zkClient: ZkClient, tp: TopicAndPartition): Long = {
    val request = OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))

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
