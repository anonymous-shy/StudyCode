package xyz.shy.spark163.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.control.Breaks._

/**
  * Created by Shy on 2018/12/11
  */

object Median {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
    val data = sc.parallelize(Array(68648647, 68648809, 68667156, 68683257, 68683311, 68683369, 68683391, 68683422, 68683534, 68683630))
    //    val data = sc.parallelize(Array(1, 2, 3, 4, 5, 6, 8, 9, 11, 12, 13, 15, 18, 20, 22, 23, 25, 27, 29))
    val m = median2(data = data)
  }

  def median1(data: RDD[Int]): Int = {
    val mappeddata = data.map(x => (x / 4, x)).sortByKey()

    //p_count为每个分组的个数
    val p_count = data.map(x => (x / 4, 1)).reduceByKey(_ + _).sortByKey()
    //p_count是一个RDD，不能进行Map集合操作，所以要通过collectAsMap方法将其转换成scala的集合
    val scala_p_count = p_count.collectAsMap()
    scala_p_count.foreach(println)
    //sum_count是统计总的个数，不能用count(),因为会得到多少个map对。
    val sum_count = p_count.map(x => x._2).sum().toInt
    var temp = 0
    //中值所在的区间累加的个数
    var temp2 = 0
    //中值所在区间的前面所有的区间累加的个数
    var index = 0
    //中值的区间
    var mid = 0
    if (sum_count % 2 != 0) {
      mid = sum_count / 2 + 1 //中值在整个数据的偏移量
    }
    else {
      mid = sum_count / 2
    }
    val pcount = p_count.count()
    println(s"pcount --->>> $pcount")
    breakable {
      for (i <- 0 until pcount.toInt) {
        temp = temp + scala_p_count(i)
        temp2 = temp - scala_p_count(i)
        if (temp >= mid) {
          index = i
          break
        }
      }
    }
    //中位数在桶中的偏移量
    val offset = mid - temp2
    //takeOrdered它默认可以将key从小到大排序后，获取rdd中的前n个元素
    val result = mappeddata.filter(x => x._1 == index).takeOrdered(offset)
    val res_median = result(offset - 1)._2
    res_median
  }


  def median2(data: RDD[Int]): Int = {
    val sorted = data.sortBy(identity).zipWithIndex().map {
      case (v, idx) => (idx, v)
    }
    sorted.foreach(println)
    val count = sorted.count()
    val median: Int = if (count % 2 == 0) {
      val l = count / 2 - 1
      val r = l + 1
      (sorted.lookup(l).head + sorted.lookup(r).head) / 2
    } else sorted.lookup(count / 2).head
    println(s"Median2 = $median")
    median
  }
}
