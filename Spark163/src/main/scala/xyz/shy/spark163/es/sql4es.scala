package xyz.shy.spark163.es

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.Map

/**
  * Created by Shy on 2018/6/21
  */

object sql4es {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName(getClass.getSimpleName)
    val sc = new SparkContext(conf)
    val esCfg = Map(
      "es.nodes" -> "tagtic-slave01,tagtic-slave02,tagtic-slave03",
      "es.port" -> "9200",
      "es.mapping.id" -> "firstname",
      "es.read.field.include" -> "account_number,balance,firstname,",
      //      "es.read.field.exclude" -> "lastname,address",
      "es.scroll.size" -> "1000"
      // "es.input.max.docs.per.partition" -> "100000"
      // "es.input.use.sliced.partitions" -> "true" ? shard partitions
    )
  }
}
