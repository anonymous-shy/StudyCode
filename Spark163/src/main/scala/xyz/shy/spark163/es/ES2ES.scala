package xyz.shy.spark163.es

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext
import xyz.shy.spark163.utils.CommonUtil
import org.elasticsearch.spark._

/**
  * Created by Shy on 2018/5/25
  */

object ES2ES {

  def main(args: Array[String]): Unit = {
    val resConf = ConfigFactory.load()
    val conf = new SparkConf()
      .setMaster("local[*]")
    conf.set("es.scroll.size", "500")
    conf.set("spark.driver.allowMultipleContexts", "true")
    conf.set("es.nodes", "tagtic-slave01,tagtic-slave02,tagtic-slave03")
    conf.set("es.port", "9200")
    conf.setAppName(getClass.getSimpleName)
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    //    val newsDF = sqlContext.read.format("org.elasticsearch.spark.sql").load("game_lib_news")
    //    newsDF.show(1)
    val query =
    """
      |{
      |  "size": 20,
      |  "sort": [
      |    {
      |      "publish_time": {
      |        "order": "desc"
      |      }
      |    }
      |  ]
      |}
    """.stripMargin
    val newsRdd = sc.esRDD("game_lib_news")
    newsRdd.cache()
    println(newsRdd.count())
  }
}
