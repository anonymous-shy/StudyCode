package xyz.shy.spark163.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/4/28.
  */
object MapPartitionsTest extends App {

  def statistic_func(data: RDD[String], statisticName: String, filter: String*) = {
    data.filter(line => {
      if (filter.size == 1)
        line.contains(filter(0))
      else
        line.contains(filter(0)) && line.contains(filter(1))
    }).map(x => {
      val arr = x.split(";")
      val data_source_id = arr(0).split("=")(1).trim
      val url = arr(1).split("=")(1).trim
      val id = arr(2).split("=")(1).trim
      (data_source_id + "#" + id, 1)
    }).reduceByKey(_ + _) //.map(t => (t._1, s"$statisticName=" + t._2))
      .mapPartitions(x => {
//      var res = List[(String, String)]()
      val res = scala.collection.mutable.ListBuffer[(String, String)]()
      while (x.hasNext){
        println(x.next()._1)
        println(x.next()._2)
        res += ((x.next()._1, s"$statisticName=" + x.next()._2))
      }
      println(s"res.size = ${res.size}")
      res.iterator
    })
  }

  val conf = new SparkConf().setAppName("PraseSpiderLog")
  val sc = new SparkContext(conf)
  val file_path = "/user/shy/download_img_slave08_20170428T044502.log"
  val data = sc.textFile(file_path)
  private val next_page_url_num = statistic_func(data, "next_page_url_num", "lpush to redis data :", "parse_function = parse_list_page")
  next_page_url_num.foreach(println)
}
