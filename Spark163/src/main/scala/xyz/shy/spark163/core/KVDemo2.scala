package xyz.shy.spark163.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2018/5/5
  */

object KVDemo2 extends App {

  val conf = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  val rdd1: RDD[(String, Int)] = sc.parallelize(Array(("A", 1), ("A", 2), ("B", 1), ("B", 2), ("C", 1)))
  /**
    * combineByKey
    * def combineByKey[C](createCombiner: (V) => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C): RDD[(K, C)]
    * def combineByKey[C](createCombiner: (V) => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C, numPartitions: Int): RDD[(K, C)]
    * def combineByKey[C](createCombiner: (V) => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C, partitioner: Partitioner, mapSideCombine: Boolean = true, serializer: Serializer = null): RDD[(K, C)]
    *
    */
  //TODO
}
