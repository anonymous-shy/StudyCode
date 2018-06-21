package xyz.shy.spark163.es

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark.rdd.EsSpark
import xyz.shy.spark163.streaming.StreamingExamples
import xyz.shy.spark163.utils.StreamingUtils

/**
  * Created by Shy on 2018/5/25
  */

object streaming4es {

  def main(args: Array[String]): Unit = {
    val resConf = ConfigFactory.load()
    val confTopic: String = resConf.getString("KafkaTopics")
    val topics = Seq[String](confTopic)
    //    confTopic.split(",").foreach(t => topics += t)
    val group = resConf.getString("KafkaGroup")
    val zkQuorums = resConf.getString("com.ZKNodes")
    val brokerList = resConf.getString("com.KafkaBrokers")
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "zookeeper.connect" -> zkQuorums
    )
    val esCfg = Map(
      "pushdown" -> "true",
      "es.nodes" -> "tagtic-slave01,tagtic-slave02,tagtic-slave03",
      "es.port" -> "9200",
      "es.mapping.id" -> "id")
    StreamingExamples.setStreamingLogLevels()

    val sparkConf = new SparkConf().setAppName("test-news")
    sparkConf.setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val kafkaStream = StreamingUtils.CreateKafkaDirectStream(ssc, kafkaParams, zkQuorums, topics)

    var offsetRanges = Array[OffsetRange]()
    kafkaStream.transform { rdd =>
      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd
    }.map(_._2)
      .foreachRDD(rdd => {
        // 同RDD
        EsSpark.saveJsonToEs(rdd, "game_lib_news/_doc", esCfg)

        StreamingUtils.saveOffsets(offsetRanges, kafkaParams, zkQuorums)
      })
    //    EsSparkStreaming.saveJsonToEs(ds, "game_lib_news/_doc", esCfg)
    ssc.start()
    ssc.awaitTermination()
  }
}
