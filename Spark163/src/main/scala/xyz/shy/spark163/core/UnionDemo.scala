package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/3/23.
  */
object UnionDemo extends App {

  val conf = new SparkConf()
    .setAppName("UnionDemo")
    .setMaster("local")
  val sc = new SparkContext(conf)
  val rdd1 = sc.makeRDD(Range(1, 10))
  val rdd2 = sc.makeRDD(Range(5, 15))
  //union 将两个RDD进行合并,不去重.
  rdd1.union(rdd2).foreach(println(_))
  println("~~~~~~~~~~~~~~Union~~~~~~~~~~~~~~~~~~")
  //distinct 对RDD中的元素进行去重操作
  rdd1.union(rdd2).distinct.foreach(println(_))
  println("~~~~~~~~~~~~~~~distinct~~~~~~~~~~~~~~~~~")
  //intersection 返回两个RDD的交集，并且去重
  rdd1.intersection(rdd2).foreach(println(_))
  //subtract 类似于intersection,但返回在RDD中出现,并且不在otherRDD中出现的元素,不去重.
  rdd1.subtract(rdd2).foreach(println(_))
}
