package xyz.shy.spark163.utils

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{DataFrame, DataFrameReader, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by Shy on 2017/3/2.
  */
object CommonUtil {

//  def getDataFrameReader(hctx: SQLContext, query: String): DataFrameReader = {
//    val options = Map("es.nodes" -> "spider_slave05,spider_slave06,spider_slave07", "es.port" -> "9200", "es.query" -> query)
//    hctx.read.format("org.elasticsearch.spark.sql").options(options)
//  }

  def getDataFrameReader(hctx: SQLContext): DataFrameReader = {
    val options = Map("es.nodes" -> "spider_slave05,spider_slave06,spider_slave07", "es.port" -> "9200")
    hctx.read.format("org.elasticsearch.spark.sql").options(options)
  }

  def getSQLContext(className: String): SQLContext = {
    val conf = new SparkConf()
    conf.set("es.scroll.size", "500")
    conf.set("spark.driver.allowMultipleContexts", "true")
    conf.set("es.nodes", "spider_slave05,spider_slave06,spider_slave07")
    conf.set("es.port", "9200")
    conf.setAppName(className)
    val sc = new SparkContext(conf)
    new HiveContext(sc)
  }

  def saveHBase(ds: DataFrame, table: String): Unit = {
    ds.write.mode(SaveMode.Overwrite).options(
      Map("table" -> table, "zkUrl" -> "slave01:2181;slave02:2181;slave03:2181")
    ).format("org.apache.phoenix.spark").save()
  }

  def loadHBase(table: String)(implicit ctx: SQLContext): DataFrame = {
    val df = ctx.read.options(Map("table" -> table, "zkUrl" -> "slave01:2181;slave02:2181;slave03:2181")
    ).format("org.apache.phoenix.spark").load()
    df
  }

  def loadMySql(table: String)(implicit sqlContext: SQLContext): DataFrame = {
    val jdbcDF = sqlContext.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://spider_slave07:3306/niuer_data?useUnicode=true&characterEncoding=UTF-8",
        "user" -> "niuer_userdata",
        "password" -> "1q2w3e4rdns",
        "dbtable" -> table)).load()
    jdbcDF
  }

  def saveMySql(ds: DataFrame, table: String): Unit = {
    val prop = new java.util.Properties()
    prop.put("driver", "com.mysql.jdbc.Driver")
    prop.put("user", "niuer_userdata")
    prop.put("password", "1q2w3e4rdns")
    val url = "jdbc:mysql://spider_slave07:3306/niuer_data?useUnicode=true&characterEncoding=UTF-8"
    ds.write.mode("overwrite").jdbc(url, table, prop)
  }
}
