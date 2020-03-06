package xyz.shy.spark220.utils

import java.{util => ju}

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.common.TopicPartition
import org.apache.log4j.Logger
import org.apache.spark.streaming.kafka010.OffsetRange

import scala.collection.mutable.{Map => MuMap}

/**
  * 管理kafka的offset,使用存放在redis中
  * Redis 存放格式 hash KT:topic [partition -> offset]
  */
object KafkaOffsetManage {
  lazy private val LOG: Logger = Logger.getLogger(this.getClass)
  lazy private val redis = RedisClusterPool.getJedisCluster

  val resConf: Config = ConfigFactory.load()
  val GROUP_ID: String = resConf.getString("com.KafkaGroup")
  val BOOTSTRAP: String = resConf.getString("com.KafkaBrokers")
  val TOPICS: String = resConf.getString("com.KafkaTopics")
  val zkQuorums: String = resConf.getString("com.ZKNodes")

  /**
    * 获取redis的offset
    * redis key : kafka:topic:partion  value : offset
    *
    */
  /*def getRedisTopic(topics: Set[String]): Option[List[String]] = {
    //    val redis = RedisClusterPool.getJedisCluster
    var option: Option[List[String]] = null
    val ab = ArrayBuffer[String]()
    val cursor = ScanParams.SCAN_POINTER_START
    val scanParams = new ScanParams
    scanParams.count(Integer.MAX_VALUE)
    topics.foreach(topic => {
      scanParams.`match`(s"kafka:$topic:*")
      val result = redis.scan(cursor, scanParams)
      if (result.getResult.size() > 0) {
        ab ++= result.getResult
        option = Option(result.getResult)

      } else {
        option = Option(new ju.ArrayList[String]())
      }
    })
    option
  }*/

  /*def getTopicPartitionOffset(topicAndOffset: List[String]): Map[TopicPartition, Long] = {
    val fromOffsets: MuMap[TopicPartition, Long] = MuMap()
    for (key <- topicAndOffset) {
      val offset = java.lang.Long.parseLong(redis.get(String.valueOf(key)))
      val split = String.valueOf(key).split(":")

      fromOffsets += (new TopicPartition(split(1), Integer.parseInt(split(2))) -> offset)
    }
    RedisClusterPool.returnConnection(redis)
    fromOffsets.toMap
  }*/

  /**
    *
    * @param groupId Consumer Group
    * @param topics  Set(topics)
    * @return
    */
  def getTopicPartitionOffset(groupId: String, topics: Set[String]): Map[TopicPartition, Long] = {
    val fromOffsets: MuMap[TopicPartition, Long] = MuMap()
    for (topic <- topics) {
      // 查询出Redis中的所有topic partition
      val topicPartitionOffset: ju.Map[String, String] = redis.hgetAll(s"""KafkaG&T:$groupId:$topic""")
      // 判断如果 Redis 中存储的topic没有
      if (topicPartitionOffset.isEmpty) {
        val partitions4Topics: Map[String, Seq[Int]] = KafkaZKUtils.getKafkaTopicAPartitions(topic)
        partitions4Topics.foreach(tp => {
          val topic = tp._1
          val partitions = tp._2
          partitions.foreach(p => {
            fromOffsets += (new TopicPartition(topic, p) -> 0)
            redis.hset(s"""KafkaG&T:$groupId:$topic""", p.toString, "0")
          })
        })
      }
      import scala.collection.JavaConversions._
      for (entry <- topicPartitionOffset.entrySet) {
        val p = entry.getKey.toInt
        val o = entry.getValue.toInt
        fromOffsets += (new TopicPartition(topic, p) -> o)
      }
    }
    RedisClusterPool.returnConnection(redis)
    fromOffsets.toMap
  }

  /**
    * 存储 TopicPartitionOffset
    * redis hash : Key Group:Topic
    *
    * @param offsetRanges Array[OffsetRange]
    **/
  def setTopicPartitionOffset(groupId: String, offsetRanges: Array[OffsetRange]): Unit = {
    offsetRanges.foreach(o => {
      val topic = o.topic
      val partition = o.partition.toString
      val untilOffset = o.untilOffset.toString
      LOG.info(s"----> ${
        o.toString()
      } <----")
      // redis.set(s"kafka:$topic:$partition", offset)
      redis.hset(s"""KafkaG&T:$groupId:$topic""", partition, untilOffset)
      // log.info(s"Offset update: set offset of ${o.topic}/${o.partition} as ${o.untilOffset.toString}")
    })
    RedisClusterPool.returnConnection(redis)
  }
}
