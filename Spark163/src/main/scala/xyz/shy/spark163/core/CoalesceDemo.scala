package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/3/23.
  */
object CoalesceDemo extends App {

  val conf = new SparkConf()
    .setAppName("CoalesceDemo")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)

  /**
    * coalesce 将RDD的partition缩减
    * 将一定量的数据,压缩到更少的partition中
    * 建议使用场景: 配合filter算子使用
    * e.g. 使用filter算子过滤,使用filter过滤大量数据后,出现partition中数据不均匀的情况
    * 此时建议使用 coalesce 重新平衡partition
    * numPartitions: Int, shuffle: Boolean = false
    * 第一个参数为重分区的数目，第二个为是否进行shuffle，默认为false;
    * *** 如果重分区的数目大于原来的分区数，那么必须指定shuffle参数为true，否则，分区数不便 或使用 repartition
    */
  private val rdd = sc.makeRDD(Range(0, 20), 4)
  private val coalesce = rdd.mapPartitionsWithIndex((index, iter) => {
    for (i <- iter)
      println(s"Index: $index, $i")
    iter
  }).coalesce(3)
  println("RePartition Size: " + coalesce.partitions.size)

  coalesce.mapPartitionsWithIndex((index, iter) => {
    for (i <- iter)
      println(s"ReIndex: $index, $i")
    iter
  }).collect()
  println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
  /**
    * repartition 该函数其实就是coalesce函数第二个参数为true的实现
    * coalesce(numPartitions, shuffle = true)
    */
  rdd.repartition(2).mapPartitionsWithIndex((index, iter) => {
    for (i <- iter)
      println(s"ReIndex: $index, $i")
    iter
  }).collect()

}
