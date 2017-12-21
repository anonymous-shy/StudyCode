package xyz.shy.spark163.streaming.storeOffsetInZk

import com.typesafe.config.ConfigFactory
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}

/**
  * Created by Shy on 2017/12/20
  */

object SimpleDirect {

  def main(args: Array[String]): Unit = {
    val resConf = ConfigFactory.load()
    val topic: String = resConf.getString("KafkaTopics")
    //创建 stream 时使用的 topic 名字集合
    val topics: Set[String] = Set(topic)
    val group = resConf.getString("KafkaGroup")
    val zkQuorums = resConf.getString("com.ZKNodes")
    val brokerList = resConf.getString("com.KafkaBrokers")
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "zookeeper.connect" -> zkQuorums,
      "auto.offset.reset" -> kafka.api.OffsetRequest.SmallestTimeString
    )
    val sparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    //创建一个 ZKGroupTopicDirs 对象，对保存
    val topicDirs = new ZKGroupTopicDirs(group, topic)
    //获取 zookeeper 中的路径，这里会变成 /consumers/$group/offsets/topic_name
    val zkTopicPath = s"${topicDirs.consumerOffsetDir}"
    //zookeeper 的host 和 ip，创建一个 client
    val zkClient = new ZkClient(zkQuorums)
    //查询该路径下是否字节点（默认有字节点为我们自己保存不同 partition 时生成的）
    val children = zkClient.countChildren(s"${topicDirs.consumerOffsetDir}")
    var kafkaStream: InputDStream[(String, String)] = null
    //如果 zookeeper 中有保存 offset，我们会利用这个 offset 作为 kafkaStream 的起始位置
    var fromOffsets: Map[TopicAndPartition, Long] = Map()
    if (children > 0) {
      for (i <- 0 until children) {
        val partitionOffset = zkClient.readData[String](s"${topicDirs.consumerOffsetDir}/${i}")
        val tp = TopicAndPartition(topic, i)
        fromOffsets += (tp -> partitionOffset.toLong)
        println("@@@@@@ topic[" + topic + "] partition[" + i + "] offset[" + partitionOffset + "] @@@@@@")
      }
      //这个会将 kafka 的消息进行 transform，最终 kafka 的数据都会变成 (topic_name, message) 这样的 tuple
      val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message())
      kafkaStream =
        KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    } else {
      //如果未保存，根据 kafkaParam 的配置使用最新或者最旧的 offset
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    }

    var offsetRanges = Array[OffsetRange]()
    kafkaStream.transform { rdd =>
      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges //得到该 rdd 对应 kafka 的消息的 offset
      rdd
    }.map(msg => msg._2).foreachRDD { rdd =>
      for (o <- offsetRanges) {
        val zkPath = s"${topicDirs.consumerOffsetDir}/${o.partition}"
        ZkUtils.updatePersistentPath(zkClient, zkPath, o.fromOffset.toString) //将该 partition 的 offset 保存到 zookeeper
        println(s"@@@@@@ topic -> ${o.topic}  partition -> ${o.partition}  fromoffset -> ${o.fromOffset}  untiloffset -> ${o.untilOffset} @@@@@@")
      }
      rdd.foreachPartition(
        message => {
          while (message.hasNext) {
            println(s"@^_^@   [" + message.next() + "] @^_^@")
          }
        }
      )
    }
  }
}
