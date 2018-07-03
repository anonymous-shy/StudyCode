package xyz.shy.spark220.core

import org.apache.spark.sql.SparkSession

/**
  * Created by Shy on 2018/7/3
  */

object SimpleApp {

  def main(args: Array[String]): Unit = {
    val logFile = "hdfs://tagticHA/user/shy/README.md"
    val spark = SparkSession.builder
      .appName("Simple Application")
      .master("local[*]")
      .getOrCreate()
    val textFile = spark.read.textFile(logFile).cache()
    val numAs = textFile.filter(line => line.contains("a")).count()
    val numBs = textFile.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")

    val wordCounts = textFile.flatMap(_.split(" ")).groupByKey(identity).count()
    println(s"")
    spark.stop()
  }
}
