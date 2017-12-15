package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/3/23.
  */
object AggregateByKeyDemo extends App {

  val conf = new SparkConf()
    .setAppName("AggregateByKeyDemo")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)

  //合并在同一个partition中的值， a的数据类型为zeroValue的数据类型，b的数据类型为原value的数据类型
  def seqOp(a: Int, b: Int): Int = {
    println(s"seq: $a + $b")
    a + b
  }

  //合并在不同partition中的值，a,b的数据类型为zeroValue的数据类型
  def combOp(a: Int, b: Int): Int = {
    println(s"comb: $a + $b")
    a + b
  }

  sc.textFile("hdfs://tagticHA//user/shy/wordCount", 2) // "hdfs://tagticHA//user/shy/wordCount"
    .flatMap(_.split("\\s"))
    .mapPartitions(iter => {
      val res = for (e <- iter) yield (e, 1)
      res
    })

    /**
      * aggregateByKey 比较类似MR
      * reduceByKey 是 aggregateByKey 简化版
      * aggregateByKey 多提供了一个函数 seqOp
      * 即可以控制对partition中的先聚合操作,类似MR中的 map-side 的combine
      * 在对所有partition数据进行全局聚合
      * 第一个参数：每个key的初始值
      * 第二个参数：seqOp 如何进行shuffle map-side的本地聚合
      * 第三个参数：combOp 如何进行shuffle reduce-side的全局聚合
      */
    .aggregateByKey(0)(seqOp, combOp)
    .collect().foreach(println)
}
