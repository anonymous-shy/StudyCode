package xyz.shy.spark163.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Shy on 2017/12/7.
  */
object ReduceByKeyAndWindowDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))
    val dStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      Map("bootstrap.servers" -> "192.168.1.101:9092,192.168.1.102:9092,192.168.1.103:9092"),
      Set("topic1")
    ).map(_._2)
    dStream
      .map((_, 1))
      .reduceByKeyAndWindow((i1: Int, i2: Int) => i1 + i2, Seconds(30), Seconds(10))
      .transform(word => {
        val hotwords = word.map(x => (x._2, x._1)).sortByKey(false).map(x => (x._2, x._1))
        hotwords.take(3).foreach(print)
        word
      })
      .print()
    ssc.start()
    ssc.awaitTermination()
  }
}
