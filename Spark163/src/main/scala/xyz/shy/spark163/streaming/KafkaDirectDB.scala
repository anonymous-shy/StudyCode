package xyz.shy.spark163.streaming

import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.common.TopicAndPartition

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.TopicPartition
import org.apache.spark.{SparkContext, SparkConf, TaskContext, SparkException}
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream._
import org.apache.spark.streaming.kafka.{KafkaUtils, HasOffsetRanges, OffsetRange, KafkaCluster}

import com.typesafe.config.ConfigFactory
import scalikejdbc._
import scala.collection.JavaConverters._

/**
  * Created by Shy on 2017/12/14
+----------+--------------+------+-----+---------+-------+
| Field    | Type         | Null | Key | Default | Extra |
+----------+--------------+------+-----+---------+-------+
| topic    | varchar(100) | NO   | PRI | NULL    |       |
| group_id | varchar(50)  | NO   | PRI |         |       |
| part     | int(4)       | NO   | PRI | 0       |       |
| offset   | mediumtext   | YES  |     | NULL    |       |
+----------+--------------+------+-----+---------+-------+
  */
object SetupJdbc {
  def apply(driver: String, host: String, user: String, password: String): Unit = {
    Class.forName(driver)
    ConnectionPool.singleton(host, user, password)
  }
}

object KafkaDirectDB {
  def main(args: Array[String]): Unit = {

    val conf = ConfigFactory.load // 加载工程resources目录下application.conf文件，该文件中配置了databases信息，以及topic及group消息
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> conf.getString("kafka.brokers"),
      "group.id" -> conf.getString("kafka.group"),
      "auto.offset.reset" -> "smallest"
    )
    val jdbcDriver = conf.getString("jdbc.driver")
    val jdbcUrl = conf.getString("jdbc.url")
    val jdbcUser = conf.getString("jdbc.user")
    val jdbcPassword = conf.getString("jdbc.password")

    val topic = conf.getString("kafka.topics")
    val group = conf.getString("kafka.group")

    val ssc = setupSsc(kafkaParams, jdbcDriver, jdbcUrl, jdbcUser, jdbcPassword, topic, group)()
    ssc.start()
    ssc.awaitTermination()
  }

  def createStream(taskOffsetInfo: Map[TopicAndPartition, Long], kafkaParams: Map[String, String], conf: SparkConf, ssc: StreamingContext, topics: String): InputDStream[_] = {
    /**
      * 若taskOffsetInfo不为空,说明这不是第一次启动该任务,database已经保存了该topic下该group的已消费的offset,
      * 则对比kafka中该topic有效的offset的最小值和数据库保存的offset,去比较大作为新的offset.
      */
    if (taskOffsetInfo.nonEmpty) {
      val kc = new KafkaClusterHelper(kafkaParams)
      val earliestLeaderOffsets = kc.getEarliestLeaderOffsets(taskOffsetInfo.keySet)
      if (earliestLeaderOffsets.isLeft)
        throw new SparkException("get kafka partition failed:")
      val earliestOffSets = earliestLeaderOffsets.right.get

      val offsets = earliestOffSets.map(r =>
        TopicAndPartition(r._1.topic, r._1.partition) -> r._2.offset.toLong)

      val newOffsets = taskOffsetInfo.map(r => {
        val t = offsets(r._1)
        if (t > r._2) {
          r._1 -> t
        } else {
          r._1 -> r._2
        }
      }
      )
      val messageHandler = (mmd: MessageAndMetadata[String, String]) => 1L
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, Long](ssc, kafkaParams, newOffsets, messageHandler) //val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    } else {
      val topicSet = topics.split(",").toSet
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)
    }
  }

  def setupSsc(
                kafkaParams: Map[String, String],
                jdbcDriver: String,
                jdbcUrl: String,
                jdbcUser: String,
                jdbcPassword: String,
                topics: String,
                group: String
              )(): StreamingContext = {

    /**
      * spark.task.maxFailures=1, Task重试次数为1，即不重试
      * spark.speculation=false 关闭推测执行,
      * 重点说下这个参数spark.speculation这个参数表示空闲的资源节点会不会尝试执行还在运行，并且运行时间过长的Task，
      * 避免单个节点运行速度过慢导致整个任务卡在一个节点上。这个参数最好设置为true。
      * 与之相配合可以一起设置的参数有spark.speculation.×开头的参数
      * (设置spark.speculation=true将执行事件过长的节点去掉并重新分配任务而spark.speculation.interval用来设置执行间隔)
      */
    val conf = new SparkConf()
      .setMaster("mesos://10.142.113.239:5050")
      .setAppName("offset")
      .set("spark.worker.timeout", "500")
      .set("spark.cores.max", "10")
      .set("spark.streaming.kafka.maxRatePerPartition", "500")
      .set("spark.rpc.askTimeout", "600s")
      .set("spark.network.timeout", "600s")
      .set("spark.streaming.backpressure.enabled", "true")
      .set("spark.task.maxFailures", "1")
      .set("spark.speculationfalse", "false")


    val ssc = new StreamingContext(conf, Seconds(5))
    SetupJdbc(jdbcDriver, jdbcUrl, jdbcUser, jdbcPassword) // connect to mysql

    // begin from the the offsets committed to the database
    val fromOffsets = DB.readOnly { implicit session =>
      sql"SELECT topic, part, offset FROM streaming_task WHERE group_id=$group".
        map { resultSet =>
          new TopicAndPartition(resultSet.string(1), resultSet.int(2)) -> resultSet.long(3)
        }.list.apply().toMap
    }

    val stream = createStream(fromOffsets, kafkaParams, conf, ssc, topics)

    stream.foreachRDD { rdd =>
      if (rdd.count != 0) {
        // you task
        val t = rdd.map(record => (record, 1))
        val results = t.reduceByKey {
          _ + _
        }.collect


        // persist the offset into the database
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        DB.localTx { implicit session =>
          offsetRanges.foreach { osr =>
            sql"""REPLACE INTO streaming_task VALUES(${osr.topic}, ${group}, ${osr.partition}, ${osr.untilOffset})""".update.apply()
            if (osr.partition == 0) {
              println(osr.partition, osr.untilOffset)
            }
          }
        }
      }
    }
    ssc
  }
}
