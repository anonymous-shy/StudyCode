package xyz.shy.spark163.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Shy on 2017/12/7.
  */
object KafkaDirect {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))
    /**
      * Create an input stream that directly pulls messages from Kafka Brokers
      * without using any receiver. This stream can guarantee that each message
      * from Kafka is included in transformations exactly once (see points below).
      *
      * Points to note:
      *  - No receivers: This stream does not use any receiver. It directly queries Kafka
      *  - Offsets: This does not use Zookeeper to store offsets. The consumed offsets are tracked
      * by the stream itself. For interoperability with Kafka monitoring tools that depend on
      * Zookeeper, you have to update Kafka/Zookeeper yourself from the streaming application.
      * You can access the offsets used in each batch from the generated RDDs (see
      * [[org.apache.spark.streaming.kafka.HasOffsetRanges]]).
      *  - Failure Recovery: To recover from driver failures, you have to enable checkpointing
      * in the [[StreamingContext]]. The information on consumed offset can be
      * recovered from the checkpoint. See the programming guide for details (constraints, etc.).
      *  - End-to-end semantics: This stream ensures that every records is effectively received and
      * transformed exactly once, but gives no guarantees on whether the transformed data are
      * outputted exactly once. For end-to-end exactly-once semantics, you have to either ensure
      * that the output operation is idempotent, or use transactions to output records atomically.
      * See the programming guide for more details.
      *
      * ssc         StreamingContext object
      * kafkaParams Kafka <a href="http://kafka.apache.org/documentation.html#configuration">
      * configuration parameters</a>. Requires "metadata.broker.list" or "bootstrap.servers"
      * to be set with Kafka broker(s) (NOT zookeeper servers), specified in
      * host1:port1,host2:port2 form.
      * If not starting from a checkpoint, "auto.offset.reset" may be set to "largest" or "smallest"
      * to determine where the stream starts (defaults to "largest")
      * topics      Names of the topics to consume
      * K  type of Kafka message key
      * V  type of Kafka message value
      * KD type of Kafka message key decoder
      * VD type of Kafka message value decoder
      * DStream of (Kafka message key, Kafka message value)
      */
    val dStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      Map("bootstrap.servers" -> "192.168.1.101:9092,192.168.1.102:9092,192.168.1.103:9092"),
      Set("topic1")
    )
    dStream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
