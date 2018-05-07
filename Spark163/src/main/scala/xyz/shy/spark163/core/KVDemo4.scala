package xyz.shy.spark163.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2018/5/7
  */

object KVDemo4 extends App {

  val conf = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[*]")
  val sc = new SparkContext(conf)

  val rdd1: RDD[(String, Int)] = sc.parallelize(Array(("A", 1), ("B", 2), ("C", 3), ("D", 4)), 2)
  val rdd2: RDD[(String, String)] = sc.parallelize(Array(("A", "a"), ("B", "b"), ("C", "c")), 2)

  /**
    * cogroup相当于SQL中的全外关联full outer join，返回左右RDD中的记录，关联不上的为空。
    * 参数numPartitions用于指定结果的分区数。
    * 参数partitioner用于指定分区函数。
    */
  //参数为1个RDD的例子
  val rdd3: RDD[(String, (Iterable[Int], Iterable[String]))] = rdd1.cogroup(rdd2)
  println(s"rdd3 partition size: ${rdd1.partitions.length}")
  rdd3.collect.foreach(println)
  println("~" * 50)
  /**
    * join相当于SQL中的内关联join,只返回两个RDD根据K可以关联上的结果,
    * join只能用于两个RDD之间的关联,如果要多个RDD关联,多关联几次即可.
    * 参数numPartitions用于指定结果的分区数
    * 参数partitioner用于指定分区函数
    */
  val joinRdd: RDD[(String, (Int, String))] = rdd1.join(rdd2)
  joinRdd.collect.foreach(println)
  println("~" * 50)
  /**
    * leftOuterJoin类似于SQL中的左外关联left outer join，返回结果以前面的RDD为主，关联不上的记录为空。
    * 只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
    * 参数numPartitions用于指定结果的分区数
    * 参数partitioner用于指定分区函数
    */
  val leftJoinRdd: RDD[(String, (Int, Option[String]))] = rdd1.leftOuterJoin(rdd2)
  leftJoinRdd.collect.foreach(println)
  println("~" * 50)
  /**
    * rightOuterJoin类似于SQL中的有外关联right outer join，返回结果以参数中的RDD为主，关联不上的记录为空。
    * 只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
    * 参数numPartitions用于指定结果的分区数
    * 参数partitioner用于指定分区函数
    */
  val rightJoinRdd: RDD[(String, (Option[Int], String))] = rdd1.rightOuterJoin(rdd2)
  rightJoinRdd.collect.foreach(println)
  println("~" * 50)
  /**
    * subtractByKey和基本转换操作中的subtract类似，只不过这里是针对K的，返回在主RDD中出现，并且不在otherRDD中出现的元素。
    * 参数numPartitions用于指定结果的分区数
    * 参数partitioner用于指定分区函数
    */
  val subtractRdd: RDD[(String, Int)] = rdd1.subtractByKey(rdd2)
  subtractRdd.collect.foreach(println)
}
