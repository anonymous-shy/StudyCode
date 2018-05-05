package xyz.shy.spark163.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable.{Map => MMap}

/**
  * Created by Shy on 2018/5/4
  */

object PartitionsDemo extends App {

  val conf = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  val rdd1: RDD[Int] = sc.parallelize(1 to 50)
  // 统计rdd中每个分区元素个数
  val rdd2: RDD[(String, Int)] = rdd1.mapPartitionsWithIndex((partIdx, iter) => {
    val partMap = MMap[String, Int]()
    iter.foreach(i => {
      val partName = s"part_$partIdx"
      println(s"Partition: $partName, value: $i")
      if (partMap.contains(partName)) {
        var eleCNT = partMap(partName)
        partMap(partName) = eleCNT + 1
      } else {
        partMap(partName) = 1
      }
    })
    partMap.iterator
  })
  rdd2.collect.foreach(println)
  println("~" * 50)
  // 统计rdd中每个分区元素
  val rdd3: RDD[(String, List[Int])] = rdd1.mapPartitionsWithIndex((idx, iter) => {
    val partMap = MMap[String, List[Int]]()
    iter.foreach(i => {
      val partName = s"part_$idx"
      println(s"Partition: $partName, value: $i")
      if (partMap.contains(partName)) {
        var elems = partMap(partName)
        elems ::= i // <=> elems = i :: elems ::只对List有效
        partMap(partName) = elems
      } else
        partMap(partName) = List[Int](i)
    })
    partMap.iterator
  })
  rdd3.collect.foreach(println)
}
