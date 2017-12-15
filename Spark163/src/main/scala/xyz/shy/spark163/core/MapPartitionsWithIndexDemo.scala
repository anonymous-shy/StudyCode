package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/3/23.
  */
object MapPartitionsWithIndexDemo extends App {

  val conf = new SparkConf()
    .setAppName("MapPartitionsWithIndexDemo")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  //指定 parallelize 并行集合时,指定 numParatitions 是 4
  //即 100 个数字分为 4个 paratition
  private val parallelizeRdd = sc.parallelize(Range(1, 101), 4)

  //mapPartitionsWithIndex 在 mapPartitions 同时返回了每个 partition分区的 索引index
  private val mapPartitionsWithIndexRdd = parallelizeRdd.mapPartitionsWithIndex((x, iter) => {
    var res = List[String]()
    var i = 0
    while (iter.hasNext) {
      i += iter.next()
    }
    res.::(s"$x-$i").iterator
  }, true)
  mapPartitionsWithIndexRdd.foreach(println(_))

}
