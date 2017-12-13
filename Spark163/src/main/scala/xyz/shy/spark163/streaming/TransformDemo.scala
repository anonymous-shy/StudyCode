package xyz.shy.spark163.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Shy on 2017/12/7.
  */
object TransformDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))
    val balckList = sc.broadcast[List[String]](List("AnonYmous", "None"))
    val dStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      Map("bootstrap.servers" -> "192.168.1.101:9092,192.168.1.102:9092,192.168.1.103:9092"),
      Set("topic1")
    ).map(_._2)
    val transform = dStream.transform(userRdd => {
      userRdd.map(x => (x.split("::")(2), x))
        .filter(x => !balckList.value.contains(x._1))
        .map(_._2)
    })
    transform.print()
    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }
}
