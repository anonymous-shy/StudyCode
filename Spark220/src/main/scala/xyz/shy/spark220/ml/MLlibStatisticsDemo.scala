package xyz.shy.spark220.ml

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.sql.SparkSession


/**
  * MLlib Statistics 是基础统计模块,是对RDD格式数据进行统计,包括: 汇总统计、相关系数、分层抽样、假设检验、随机数据生成等
  *
  *
  */
object MLlibStatisticsDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()
    val sc = spark.sparkContext
    val observations = sc.parallelize(
      Seq(
        Vectors.dense(1.0, 10.0, 100.0),
        Vectors.dense(2.0, 20.0, 200.0),
        Vectors.dense(3.0, 30.0, 300.0)
      )
    )
    // Compute column summary statistics.
    val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)
    println(summary.mean) // a dense vector containing the mean value for each column
    println(summary.variance) // column-wise variance
    println(summary.numNonzeros) // number of nonzeros in each column
  }
}
