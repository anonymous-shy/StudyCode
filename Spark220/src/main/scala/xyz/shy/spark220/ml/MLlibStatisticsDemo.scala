package xyz.shy.spark220.ml

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

  }
}
