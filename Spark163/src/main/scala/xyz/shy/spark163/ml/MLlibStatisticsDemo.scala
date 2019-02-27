package xyz.shy.spark163.ml


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.Statistics

/**
  * MLlib Statistics 是基础统计模块 是对 格式数据进行统计,包括: 汇总统计、相关系数、分层抽样、假设检验、随机数据生成等
  */
object MLlibStatisticsDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val sc = new SparkContext(conf)
    val data_path = "/user/shy/Stat1"
    val data = sc.textFile(s"$data_path").map(_.split("\\s")).map(f => f.map(f => f.toDouble))
    val data1 = data.map(f => Vectors.dense(f))
    val stat = Statistics.colStats(data1)
    println(stat.max)
    println(stat.min)
    println(stat.mean)
    println(stat.variance)  // 方差
    println(stat.normL1)  // L1范数
    println(stat.normL2)  // L2范数
    Statistics.corr(data1,"pearson")  // Pearson相关系数
    Statistics.corr(data1,"spearman") // Spearman相关系数

  }
}
