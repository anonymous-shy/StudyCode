package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

/**
  * Created by Shy on 2017/3/23.
  */
object MapPartitionsDemo extends App {

  val conf = new SparkConf()
    .setAppName("MapPartitionsDemo")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)

  //Mock Data
  //  val stars = Array("Shy", "Dilraba", "Taylor", "Emma", "AnonYmous")
  //  private val starRdd = sc.parallelize(stars, 2)
  //  final val starsMap = Map("Shy" -> 99, "Dilraba" -> 88, "Taylor" -> 60, "Emma" -> 98, "AnonYmous" -> 1)
  val parallelizeRdd = sc.parallelize(Range(1, 101), 4)
  /**
    * mapParatitions:
    * 类似 map,不同于 map算子一次只能处理一个partition中的一条数据
    * mapParatitions算子一次处理一个partition中的所有数据
    * 使用场景:
    * 如果RDD的数据量不是特别大,建议使用 mapParatitions 算子代替 map算子,可以加快处理速度
    * 但如果RDD数据量特别大,不建议使用,可能会内存溢出
    */
  val partitionsRdd = parallelizeRdd.mapPartitions(x => {
    var res = List[Int]()
    var i = 0
    while (x.hasNext) {
      i += x.next()
    }
    println(s"i=$i")
    res.::(i).iterator
    //    println(res)
    //    res.iterator
  })
  partitionsRdd.collect.foreach(println)
  println(s"Paration个数: " + partitionsRdd.partitions.size)
}

object MapPartitionsOper extends App {
  val conf = new SparkConf()
    .setAppName("MapPartitionsOper")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  val starArr = Array("Shy", "Emma", "Taylor")
  val starRdd = sc.parallelize(starArr)
  private val result = starRdd.mapPartitions(iter => {
    val list = ListBuffer[String]()
    while (iter.hasNext) {
      list += iter.next() + "<--->"
    }
    list.iterator
  })
  result.foreach(println)
}
