package xyz.shy.spark163.es

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark.sql._

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
    val sqlContext = new SQLContext(sc)
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
        |  }
        |}
      """.stripMargin
    val esCfg = Map(
      "es.nodes" -> "tagtic-slave01,tagtic-slave02,tagtic-slave03",
      "es.port" -> "9200",
      "es.mapping.id" -> "firstname",
      "es.read.field.include" -> "account_number,balance,firstname,",
      // "es.read.field.exclude" -> "lastname,address",
      "es.scroll.size" -> "1000",
      "es.query" -> query
    )
    //    val bankDF = sqlContext.esDF("bank", query, esCfg)
    val df = sqlContext.read.format("org.elasticsearch.spark.sql")
      .options(esCfg).load("bank")

    df.printSchema()
    // root
    //|-- departure: string (nullable = true)
    //|-- arrival: string (nullable = true)
    //|-- days: long (nullable = true)

    df.saveToEs("sql_bank/_doc", esCfg)
  }
}
