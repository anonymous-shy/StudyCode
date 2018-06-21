package xyz.shy.spark163.es

import java.time.LocalDate

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by Shy on 2018/5/25
  */

case class Game(doc_id: String, dbkey: String, publish_time: Long)

object GameLibES2ES {

  val LOG: Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    val resConf = ConfigFactory.load()
    val esNode = resConf.getString("es.nodes")
    val esPort = resConf.getString("es.port")
    val prop = new java.util.Properties()
    prop.put("driver", resConf.getString("db.default.driver"))
    prop.put("user", resConf.getString("db.default.user"))
    prop.put("password", resConf.getString("db.default.password"))
    val esdbUrl = resConf.getString("db.default.url")
    val esCfg = Map(
      "es.nodes" -> esNode,
      "es.port" -> esPort,
      "es.mapping.id" -> "doc_id")
    val sparkConf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.sql.shuffle.partitions", "40")
    sparkConf.registerKryoClasses(Array(classOf[Game]))
    val sc = new SparkContext(sparkConf)
    //    val sqlContext = new SQLContext(sc)
    val hiveContext = new HiveContext(sc)
    val indexes = Array("game_lib_news", "game_lib_weibo")
//    val query =
//      """
//        |{
//        |  "query": {
//        |    "constant_score": {
//        |      "filter": {
//        |        "range": {
//        |          "timestamp": {
//        |            "gte": "now+7h"
//        |          }
//        |        }
//        |      }
//        |    }
//        |  }
//        |}
//      """.stripMargin
    for (idx <- indexes) {
      val curMonth = LocalDate.now.toString.replace("-", "").substring(0, 6)
      val index = s"${idx}_$curMonth"
      val newsRdd = sc.esRDD(index, esCfg)

      newsRdd.take(1)
      newsRdd.persist(StorageLevel.MEMORY_ONLY_SER)
      import hiveContext.implicits._
      val newsDF = newsRdd.map(x => {
        val doc_id: String = x._1
        val newsMap = x._2
        val dbkey: String = newsMap.getOrElse("dbkey", "").asInstanceOf[String]
        val publish_time: Long = newsMap.getOrElse("publish_time", 1514736000L).asInstanceOf[Long]
        (doc_id, dbkey, publish_time)
      }).map(x => Game(x._1, x._2, x._3)).toDF()
      newsDF.registerTempTable("news")
      val top20NewsId = hiveContext.sql(
        s"""
           |SELECT
           | doc_id
           |FROM
           |  (SELECT
           |    doc_id,
           |    row_number() OVER (PARTITION BY dbkey ORDER BY publish_time DESC) AS rank
           |   FROM news) t
           |WHERE t.rank <= 20
       """.stripMargin).rdd.map(row => row.getAs[String]("doc_id")).collect
      val broadcastTop20NewsId = sc.broadcast(top20NewsId)
      newsRdd.filter(x => broadcastTop20NewsId.value.contains(x._1))
        .map(_._2)
        .saveToEs(s"${idx}_publish/_doc", esCfg)

      val publishRdd = sc.esRDD(s"${idx}_publish/_doc", esCfg)
      val publishDF = publishRdd.map(x => {
        val doc_id: String = x._1
        val newsMap = x._2
        val dbkey: String = newsMap.getOrElse("dbkey", "").asInstanceOf[String]
        val publish_time: Long = newsMap.getOrElse("publish_time", 1514736000L).asInstanceOf[Long]
        (doc_id, dbkey, publish_time)
      }).map(x => Game(x._1, x._2, x._3)).toDF()
      publishDF.registerTempTable("publish")
      hiveContext.sql(
        s"""
           |SELECT
           | '${idx}_publish' AS index,
           | doc_id
           |FROM
           |  (SELECT
           |    doc_id,
           |    row_number() OVER (PARTITION BY dbkey ORDER BY publish_time DESC) AS rank
           |   FROM publish) t
           |WHERE t.rank > 20
         """.stripMargin)
        .write.mode("overwrite").jdbc(esdbUrl, "top20docid", prop)

    }
  }
}
