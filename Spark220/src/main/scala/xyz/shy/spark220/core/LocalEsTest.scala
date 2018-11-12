package xyz.shy.spark220.core

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.rdd.EsSpark

import scala.collection.Map

/**
  * Created by Shy on 2018/9/17
  */

object LocalEsTest {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.es.index.auto.create", "true")
      .set("es.nodes", "tagtic-master")
      .set("es.port", "8888")
      .set("es.net.http.auth.user","donews")
      .set("es.net.http.auth.pass", "donews")
      .set("es.resource", "twitter")
      .set("es.nodes.wan.only", "true")
    val spark = SparkSession.builder
      .config(sparkConf)
      .appName("Es Test")
      .master("local[*]")
      .getOrCreate()
    val esCfg = Map(
      "es.nodes" -> "tagtic-master",
      "es.port" -> "8888",
      "es.nodes.discovery" -> "true",
      "es.net.http.auth.user" -> "donews",
      "es.net.http.auth.pass" -> "donews",
      "es.nodes.wan.only" -> "false"
    )
    val res = EsSpark.esRDD(spark.sparkContext)
    res.foreach(println)
  }
}
