package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/3/22.
  * 测试闭包
  */
object PlusClosureVariable extends App {

  val conf = new SparkConf()
    .setAppName("PlusClosureVariable")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)

  private val parallelize = sc.parallelize(Range(1, 6))
  final var c = 0
  parallelize.foreach(x => {
    c = x + c
    println(s"闭包变量 $c")
  })
  println(c)
}
