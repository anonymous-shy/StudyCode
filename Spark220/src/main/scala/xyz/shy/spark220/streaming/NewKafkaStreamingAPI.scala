package xyz.shy.spark220.streaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import xyz.shy.spark220.examples.streaming.StreamingExamples

/**
  * Created by Shy on 2018/12/26
  */

object NewKafkaStreamingAPI {

  def main(args: Array[String]): Unit = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092,anotherhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "GROUP_Shy",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    StreamingExamples.setStreamingLogLevels()
    val conf = new SparkConf().setMaster("local[*]").setAppName(getClass.getSimpleName)
    val streamingContext = new StreamingContext(conf, Seconds(5))
    val topics = Array("topicA", "topicB")

    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    //    stream.map(record => (record.key, record.value))

    stream.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd.foreachPartition { iter =>
          val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
          iter.foreach(record => println(record.toString))
          println(s"========> $time <========")
        }
      }
    }
  }
}
