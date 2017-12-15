package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/3/23.
  */
object ZipDemo extends App {

  val conf = new SparkConf()
    .setAppName("ZipDemo")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  private val rdd1 = sc.makeRDD(1 to 5, 2)
  private val rdd2 = sc.makeRDD(Seq("A", "B", "C", "D", "E"), 2)
  /**
    * zip函数用于将两个RDD组合成Key/Value形式的RDD,
    * 这里默认两个RDD的partition数量以及元素数量都相同,否则会抛出异常
    */
  rdd1.zip(rdd2).foreach(println)
  rdd2.zip(rdd1).foreach(println)

  //zipWithIndex 将RDD中的元素和这个元素在RDD中的ID（索引号）组合成键/值对
  rdd2.zipWithIndex().foreach(println)

  /**
    * def zipWithUniqueId(): RDD[(T, Long)]
    * 该函数将RDD中元素和一个唯一ID组合成键/值对，该唯一ID生成算法如下：
    * 每个分区中第一个元素的唯一ID值为：该分区索引号，
    * 每个分区中第N个元素的唯一ID值为：(前一个元素的唯一ID值) + (该RDD总的分区数)
    */
  rdd2.zipWithUniqueId().foreach(println)

  println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
  rdd1.mapPartitionsWithIndex {
    (index, iter) => {
      var res = List[String]()
      while (iter.hasNext) {
        res ::= (s"part_$index-" + iter.next())
      }
      res.iterator
    }
  }.foreach(println)
}
