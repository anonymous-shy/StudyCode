package spark163.streaming

import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/12/7.
  */
object KafkaReceiver {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))
    val dStream = KafkaUtils.createStream(
      ssc, //StreamingContext object
      "192.168.1.101:2181,192.168.1.102:2181,192.168.1.103:2181", //Zookeeper quorum (hostname:port,hostname:port,..)
      "group-spark", //The group id for this consumer
      Map("topic1" -> 2) //Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread
    )
    dStream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
