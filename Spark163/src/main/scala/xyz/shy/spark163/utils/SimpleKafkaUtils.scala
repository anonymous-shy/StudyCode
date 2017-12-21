package xyz.shy.spark163.utils

import java.util.Collections

import kafka.javaapi._
import kafka.javaapi.consumer.SimpleConsumer

import scala.collection.mutable.ListBuffer

//remove if not needed
import scala.collection.JavaConversions._

/**
  * Created by Shy on 2017/12/21
  */

object SimpleKafkaUtils {

  private val m_replicaBrokers: ListBuffer[String] = new ListBuffer[String]

  def findLeader(a_seedBrokers: Array[String],
                 a_port: Int,
                 a_topic: String,
                 a_partition: Int): PartitionMetadata = {
    var returnMetaData: PartitionMetadata = null

    for (seed <- a_seedBrokers) {
      var consumer: SimpleConsumer = null
      try {
        consumer = new SimpleConsumer(seed, a_port, 10000, 64 * 1024, "leaderLookup")
        val topics = Collections.singletonList(a_topic)
        val req = new TopicMetadataRequest(topics)
        val resp = consumer.send(req)
        val metaData = resp.topicsMetadata
        for (item <- metaData; part <- item.partitionsMetadata) {
          if (part.partitionId == a_partition) {
            returnMetaData = part
            //break
          }
        }
      } catch {
        case e: Exception => println("Error communicating with Broker [" + seed + "] to find Leader for [" +
          a_topic +
          ", " +
          a_partition +
          "] Reason: " +
          e)
      } finally {
        if (consumer != null) consumer.close()
      }
    }
    if (returnMetaData != null) {
      m_replicaBrokers.clear()
      for (replica <- returnMetaData.replicas) {
        m_replicaBrokers.add(replica.host)
      }
    }
    returnMetaData
  }
}
