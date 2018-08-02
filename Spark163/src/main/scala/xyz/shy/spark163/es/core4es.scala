package xyz.shy.spark163.es

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark.rdd.EsSpark

import scala.collection.Map

/**
  * Created by Shy on 2018/6/21
  */

object core4es {

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
      // "es.read.field.exclude" -> "lastname,address",
      "es.scroll.size" -> "1000"
      // "es.input.max.docs.per.partition" -> "100000"
      // "es.input.use.sliced.partitions" -> "true" ? shard partitions
    )
    val query =
      """
        |{
        |  "query": {
        |    "constant_score": {
        |      "filter": {
        |        "range": {
        |          "balance": {
        |            "gte": 40000
        |          }
        |        }
        |      }
        |    }
        |  },
        |  "sort": [
        |    {
        |      "balance": {
        |        "order": "desc"
        |      }
        |    }
        |  ]
        |}
      """.stripMargin
    val readRdd: RDD[(String, Map[String, AnyRef])] = EsSpark.esRDD(sc, "bank", query, esCfg)
    readRdd.take(10).foreach(println)
    val valueRdd = readRdd.map(_._2)
//    EsSpark.saveToEs(valueRdd, "test_bank/_doc", esCfg)
    println("=====================")
    val readJsonRdd: RDD[String] = EsSpark.esJsonRDD(sc, "bank", query, esCfg).map(_._2)
    readJsonRdd.take(10).foreach(println)
//    EsSpark.saveJsonToEs(readJsonRdd, "json_bank/_doc", esCfg)
  }
}
