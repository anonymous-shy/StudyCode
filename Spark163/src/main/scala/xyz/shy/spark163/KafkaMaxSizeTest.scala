package xyz.shy.spark163

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import xyz.shy.spark163.streaming.StreamingExamples
import xyz.shy.spark163.utils.StreamingUtils

/**
  * Created by Shy on 2019/9/11
  */

object KafkaMaxSizeTest {
  def main(args: Array[String]) {
    /*if (args.length < 2) {
      System.err.println("Usage: NetworkWordCount <hostname> <port>")
      System.exit(1)
    }*/
    val resConf = ConfigFactory.load()
    val confTopic: String = resConf.getString("KafkaTopics")
    var topics = Seq[String]("testMaxSize")
    //    confTopic.split(",").foreach(t => topics += t)
    val group = resConf.getString("KafkaGroup")
    val zkQuorums = resConf.getString("com.ZKNodes")
    val brokerList = resConf.getString("com.KafkaBrokers")
    var kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "zookeeper.connect" -> zkQuorums,
      "fetch.message.max.bytes" -> "1028000000"
    )
    StreamingExamples.setStreamingLogLevels()

    // Create the context with a 2 second batch size
    val sparkConf = new SparkConf()
      .setAppName(getClass.getName)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer") // Kryo
      .set("spark.streaming.stopGracefullyOnShutdown", "true")
      .set("spark.streaming.backpressure.enabled", "true")
      .set("spark.streaming.backpressure.initialRate", "5000")
      .set("spark.streaming.kafka.maxRatePerPartition", "2000")
      .set("spark.streaming.concurrentJobs", "5")
    sparkConf.setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val kafkaStream = StreamingUtils.CreateKafkaDirectStream(ssc, kafkaParams, zkQuorums, topics)

    var offsetRanges = Array[OffsetRange]()
    kafkaStream.transform { rdd =>
      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd
    }.map(_._2)
      .flatMap(_.split("::"))
      .foreachRDD((rdd: RDD[String], time: Time) => {
        rdd.map(s => s.length).collect.foreach(println)
        //        rdd.map(s => s.length).collect()
        println(s"========= ${time} =========")
        StreamingUtils.saveOffsets(offsetRanges, kafkaParams, zkQuorums)
      })

    ssc.start()
    ssc.awaitTermination()
  }
}
