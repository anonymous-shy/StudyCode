package xyz.shy.spark163.streaming.storeOffsetInZk

import com.typesafe.config.ConfigFactory
import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo, TopicMetadataRequest}
import kafka.common.TopicAndPartition
import kafka.consumer.SimpleConsumer
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Shy on 2017/12/14
  * https://aseigneurin.github.io/2016/05/07/spark-kafka-achieving-zero-data-loss.html
  */

object KafkaDirectStoreZK {
  def createContext(checkpointDirectory: String) = {
    val resConf = ConfigFactory.load()
    println("----->Create SparkContext<-----")
    val topics = resConf.getString("KafkaTopics")
    val group = resConf.getString("KafkaGroup")
    val zkQuorums = resConf.getString("com.ZKNodes")
    val brokerList = resConf.getString("com.KafkaBrokers")
    //    val Array(topics, group, zkQuorum,brokerList) = args
    val sparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    //    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "1")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    //    ssc.checkpoint(checkpointDirectory)
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "zookeeper.connect" -> zkQuorums,
      "auto.offset.reset" -> kafka.api.OffsetRequest.SmallestTimeString
    )
    /**
      * kafka.utils.ZkUtils
        val ConsumersPath = "/consumers"
        val BrokerIdsPath = "/brokers/ids"
        val BrokerTopicsPath = "/brokers/topics"
        val TopicConfigPath = "/config/topics"
        val TopicConfigChangesPath = "/config/changes"
        val ControllerPath = "/controller"
        val ControllerEpochPath = "/controller_epoch"
        val ReassignPartitionsPath = "/admin/reassign_partitions"
        val DeleteTopicsPath = "/admin/delete_topics"
        val PreferredReplicaLeaderElectionPath = "/admin/preferred_replica_election"

        class ZKGroupTopicDirs(group: String, topic: String) extends ZKGroupDirs(group) {
          def consumerOffsetDir = consumerGroupDir + "/offsets/" + topic
          def consumerOwnerDir = consumerGroupDir + "/owners/" + topic
        }
      */
    val topicDirs = new ZKGroupTopicDirs(group, topics)
    val zkTopicPath = s"${topicDirs.consumerOffsetDir}"
    val zkClient = new ZkClient(zkQuorums)
    val children = zkClient.countChildren(zkTopicPath)
    var kafkaStream: InputDStream[(String, String)] = null
    var fromOffsets: Map[TopicAndPartition, Long] = Map()
    if (children > 0) {
      //---get partition leader begin----
      val topicList = List(topics)
      val req = new TopicMetadataRequest(topicList, 0) //得到该topic的一些信息，比如broker,partition分布情况
      val getLeaderConsumer = new SimpleConsumer("tagtic-master", 8092, 10000, 10000, "OffsetLookup") // low level api interface
      val res = getLeaderConsumer.send(req) //TopicMetadataRequest   topic broker partition 的一些信息
      val topicMetaOption = res.topicsMetadata.headOption
      val partitions = topicMetaOption match {
        case Some(tm) =>
          tm.partitionsMetadata.map(pm => (pm.partitionId, pm.leader.get.host)).toMap[Int, String]
        case None =>
          Map[Int, String]()
      }
      //--get partition leader  end----
      for (i <- 0 until children) {
        val partitionOffset = zkClient.readData[String](s"${topicDirs.consumerOffsetDir}/$i")
        val tp = TopicAndPartition(topics, i)
        //---additional begin-----
        val requestMin = OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1))) // -2,1
        val consumerMin = new SimpleConsumer(partitions(i), 8092, 10000, 10000, "getMinOffset")
        val curOffsets = consumerMin.getOffsetsBefore(requestMin).partitionErrorAndOffsets(tp).offsets
        var nextOffset = partitionOffset.toLong
        if (curOffsets.nonEmpty && nextOffset < curOffsets.head) { //如果下一个offset小于当前的offset
          nextOffset = curOffsets.head
        }
        //---additional end-----
        fromOffsets += (tp -> nextOffset)
      }
      val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message())
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    } else {
      println("create")
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    }
    var offsetRanges = Array[OffsetRange]()
    kafkaStream.transform {
      rdd =>
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
    }.map(msg => msg._2).foreachRDD { rdd =>
      for (offset <- offsetRanges) {
        val zkPath = s"${topicDirs.consumerOffsetDir}/${offset.partition}"
        ZkUtils.updatePersistentPath(zkClient, zkPath, offset.fromOffset.toString)
      }
      rdd.foreachPartition(
        message => {
          while (message.hasNext) {
            println(message.next())
          }
        })
    }
    ssc
  }

  def main(args: Array[String]) {

    val checkpointDirectory = "kafka-checkpoint2"
    System.setProperty("hadoop.home.dir", "D:\\Program Files\\hadoop-2.2.0")
    val ssc = StreamingContext.getOrCreate(checkpointDirectory,
      () => {
        createContext(checkpointDirectory)
      })
    ssc.start()
    ssc.awaitTermination()
  }
}
