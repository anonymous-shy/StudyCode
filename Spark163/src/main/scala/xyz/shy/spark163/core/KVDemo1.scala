package xyz.shy.spark163.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

import scala.collection.mutable.{Map => MMap}

/**
  * Created by Shy on 2018/5/5
  */

object KVDemo1 extends App {

  val conf = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  val rdd1: RDD[(Int, String)] = sc.parallelize(Array((1, "A"), (2, "B"), (3, "C"), (4, "D")), 2)
  println(s"原始Partition Size: ${rdd1.partitions.length}")

  val rdd1P: RDD[(String, List[(Int, String)])] = rdd1.mapPartitionsWithIndex((idx, iter) => {
    val partMap = MMap[String, List[(Int, String)]]()
    iter.foreach(t => {
      val partName = s"part_$idx"
      println(s"Partition: $partName, value: $t")
      if (partMap.contains(partName)) {
        var elems: List[(Int, String)] = partMap(partName)
        println(s"Before ===>>> elems value: $elems, elems size: ${elems.size}")
        elems ::= t
        println(s"After ===>>> elems value: $elems, elems size: ${elems.size}")
        partMap(partName) = elems
      } else {
        partMap(partName) = List[(Int, String)](t)
      }
      //      var elems = partMap.getOrElse(partName, List[(Int, String)]())
      //      println(s"Before ===>>> elems value: $elems, elems size: ${elems.size}")
      //      elems ::= t
      //      println(s"After ===>>> elems value: $elems, elems size: ${elems.size}")
    })
    println(partMap)
    partMap.iterator
  })

  rdd1P.collect.foreach(println)
  println("~" * 50)
  /**
    * 1. partitionBy
    * def partitionBy(partitioner: Partitioner): RDD[(K, V)]
    * 该函数根据partitioner函数生成新的ShuffleRDD，将原RDD重新分区。
    */
  val rdd2: RDD[(Int, String)] = rdd1.partitionBy(new HashPartitioner(2))
  println(rdd2.partitions.length)

  val rdd2P: RDD[(String, List[(Int, String)])] = rdd2.mapPartitionsWithIndex((idx, iter) => {
    val partMap = MMap[String, List[(Int, String)]]()
    iter.foreach(t => {
      val partName = s"part_$idx"
      if (partMap.contains(partName)) {
        var elems = partMap(partName)
        elems :+= t
        partMap(partName) = elems
      } else {
        partMap(partName) = List[(Int, String)](t)
      }
    })
    partMap.iterator
  })
  rdd2P.collect.foreach(println)
  println("~" * 50)
  /**
    * mapValues
    * def mapValues[U](f: (V) => U): RDD[(K, U)]
    * 同map,只不过mapValues是针对(k,v)中v的值就行map操作
    */
  val rdd3: RDD[(Int, String)] = rdd1.mapValues(s => s + ">@_@")
  rdd3.collect.foreach(println)
  println("~" * 50)
  /**
    * flatMapValues
    * def flatMapValues[U](f: (V) => TraversableOnce[U]): RDD[(K, U)]
    * 同flatMap,只不过flatMapValues是针对(k,v)中v的值进行flatMap操作
    */
  val rdd4: RDD[(Int, Char)] = rdd1.flatMapValues(s => s + "❤")
  rdd4.collect.foreach(println)
}
