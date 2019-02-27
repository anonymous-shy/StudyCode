package xyz.shy.streaming

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

/**
  * Created by Shy on 2019/2/18
  */

object KafkaSource {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._

    //checkpoint配置
    env.enableCheckpointing(5000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    //设置statebackend
    //env.setStateBackend(new RocksDBStateBackend("hdfs://hadoop100:9000/flink/checkpoints",true));

    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "192.168.71.62:19092,192.168.71.63:19092,192.168.71.64:19092")
//    prop.setProperty("bootstrap.servers", "192.168.71.64:9092")
//    prop.setProperty("group.id", "shy-flink")
    prop.setProperty("group.id", "shy-flink")
    prop.setProperty("auto.offset.reset", "earliest") // DEFAULT: latest

    val flinkConsumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]("T1", new SimpleStringSchema(), prop)
    /**
      * Specifies the consumer to start reading from any committed group offsets found
      * in Zookeeper / Kafka brokers. The "group.id" property must be set in the configuration
      * properties. If no offset can be found for a partition, the behaviour in "auto.offset.reset"
      * set in the configuration properties will be used for the partition.
      *
      * <p>This method does not affect where partitions are read from when the consumer is restored
      * from a checkpoint or savepoint. When the consumer is restored from a checkpoint or
      * savepoint, only the offsets in the restored state will be used.
      *
      * @return The consumer object, to allow function chaining.
      */
    flinkConsumer.setStartFromGroupOffsets()
    val record = env.addSource(flinkConsumer)
    record.print()

    env.execute(getClass.getSimpleName)
  }
}
