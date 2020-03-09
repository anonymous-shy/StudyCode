package xyz.shy.spark220.utils

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.Logger
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, TaskContext}

object StreamUtils {
  lazy private val LOG: Logger = Logger.getLogger(this.getClass)

  val resConf: Config = ConfigFactory.load()
  val GROUP_ID: String = resConf.getString("com.kafkaGroup")
  val BOOTSTRAP: String = resConf.getString("com.kafkaBrokers")
  val TOPICS: String = resConf.getString("com.kafkaTopics")
  val zkQuorums: String = resConf.getString("com.zkNodes")
  val batchDurationMs: String = resConf.getString("com.batchDurationMs")
  val checkpointDir: String = resConf.getString("com.checkpointDir")

  def main(args: Array[String]): Unit = {

    val ssc = getSparkStreaming("Test New Streaming", batchDurationMs.toInt, checkpointDir)
    val kafkaStream = getKafkaDirectStream(ssc, TOPICS)
    service(kafkaStream)
    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * KafkaUtils.createDirectStream
    * ConsumerRecord
    *
    * @param kafkaDirectStream InputDStream[ConsumerRecord[String, String]
    */
  def service(kafkaDirectStream: InputDStream[ConsumerRecord[String, String]]): Unit = {
    kafkaDirectStream.foreachRDD((rdd, time) => {
      if (!rdd.isEmpty()) {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd.foreachPartition(partition => {
          val o = offsetRanges(TaskContext.get.partitionId)
          println(s"@@@@@@ ${o.toString} @@@@@@")
          partition.foreach { record =>
            // service coding
            val key: String = record.key()
            val value: String = record.value()
            println(key + " -> " + value)
          }
        })
        KafkaOffsetManage.setTopicPartitionOffset(GROUP_ID, offsetRanges)
      }
      println(s"========> $time <========")
    })
  }

  /**
    * 获取 kafka Direct Stream
    *
    * @param ssc    StreamingContext
    * @param topics Set(topics)
    * @return
    */
  def getKafkaDirectStream(ssc: StreamingContext, topics: String): InputDStream[ConsumerRecord[String, String]] = {
    val kafkaParams = getKafkaParams
    var kafkaStreams: InputDStream[ConsumerRecord[String, String]] = null
    val topicSet = topics.split(",").toSet
    //读取redis的offset消费 Map[TopicPartition, Long]
    val topicAndPartition = KafkaOffsetManage.getTopicPartitionOffset(GROUP_ID, topicSet)
    if (topicAndPartition.nonEmpty) {
      kafkaStreams = KafkaUtils.createDirectStream[String, String](
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](topicSet, kafkaParams, topicAndPartition)
      )
    } else {
      //从零消费
      kafkaStreams = KafkaUtils.createDirectStream[String, String](
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](topicSet, kafkaParams)
      )
    }
    kafkaStreams
  }

  /**
    * 获取sparkStreaming
    *
    * @param appName       AppName
    * @param batchDuration Batch Duration
    * @return StreamingContext
    */
  def getSparkStreaming(appName: String, batchDuration: Int, checkpointDirectory: String): StreamingContext = {
    val sparkConf = new SparkConf().setAppName(appName)
      .set("spark.streaming.backpressure.enabled", "true")
      .set("spark.streaming.backpressure.initialRate", "5000")
      .set("spark.streaming.stopGracefullyOnShutdown", "true")
      .set("spark.streaming.kafka.maxRatePerPartition", "2000")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.setMaster("local[*]")

    //创建sparkStreaming 并返回 根据check目录检查是否创建streamingContext，有的话从check中恢复
    // TODO
    //    val ssc = StreamingContext.getActiveOrCreate(checkpointDirectory,
    //      () => new StreamingContext(sparkConf, Seconds(batchDuration)))
    //    ssc.checkpoint(checkpointDirectory)
    // TEST
    val ssc = new StreamingContext(sparkConf, Seconds(batchDuration))
    ssc
  }

  /**
    * kafka配置
    *
    * @return kafkaParams
    */
  private def getKafkaParams = {

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> BOOTSTRAP,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> GROUP_ID,
      "auto.offset.reset" -> "earliest", //没有offset从头读取
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    kafkaParams
  }
}
