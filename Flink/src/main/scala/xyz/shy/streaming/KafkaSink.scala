package xyz.shy.streaming

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema

/**
  * Created by Shy on 2019/2/18
  */

object KafkaSink {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //隐式转换
    import org.apache.flink.api.scala._
    val text = env.socketTextStream("tagtic-slave02", 9001, '\n')
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
    //    prop.setProperty("bootstrap.servers", "192.168.71.62:19092,192.168.71.63:19092,192.168.71.64:19092")
    prop.setProperty("bootstrap.servers", "192.168.71.62:9092,192.168.71.63:9092,192.168.71.64:9092")
    // TODO : 泛型传递？
    val newflinkProducer = new FlinkKafkaProducer[String]("FlinkSinkTopic",
      new KeyedSerializationSchemaWrapper[String](new SimpleStringSchema),
      prop,
      FlinkKafkaProducer.Semantic.EXACTLY_ONCE)

    val flinkProducer = new FlinkKafkaProducer[String]("FlinkSinkTopic", new SimpleStringSchema(), prop)

    flinkProducer.setWriteTimestampToKafka(true)
    text.addSink(flinkProducer)

    env.execute(getClass.getSimpleName)
  }
}

