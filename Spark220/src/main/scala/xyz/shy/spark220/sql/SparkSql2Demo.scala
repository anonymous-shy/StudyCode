package xyz.shy.spark220.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by Shy on 2017/3/22.
  */
object SparkSql2Demo extends App {

  private val spark: SparkSession = SparkSession
    .builder()
    .appName("SparkSql2Demo")
    .master("local[*]")
//    .config("spark.sql.warehouse.dir", "F:\\spark-warehouse")
//    .enableHiveSupport()
    .getOrCreate()

  import spark.implicits._

  private val df: DataFrame = spark.read.json("hdfs://tagticHA/test/resources/.json")
  df.show() // 打印数据
  df.printSchema() // 打印元数据
  df.select("name").show() // select操作，典型的弱类型，untyped操作
  df.select($"name", $"age" + 1).show() // 使用表达式，scala的语法，要用$符号作为前缀
  df.filter($"age" > 21).show() // filter操作+表达式的一个应用
  df.groupBy("age").count().show() // groupBy分组，再聚合

  df.createOrReplaceTempView("people")
  spark.sql("Select * From people").show()
}
