package xyz.shy.spark220.ml

import java.util

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, RowFactory, SparkSession}

/**
  * Created by Shy on 2018/1/15
  */

object TfIdfDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

//    val data = util.Arrays.asList(
//      RowFactory.create(0.0, "I heard about Spark and i like spark"),
//      RowFactory.create(1.0, "I wish Java could use case classes for spark"),
//      RowFactory.create(2.0, "Logistic regression models of spark are neat and easy to use")
//    )
//    val schema = StructType(Array(
//      StructField("label", DoubleType, false),
//      StructField("sentence", StringType, false)
//    ))
//
//    spark.createDataFrame(data, schema)
  }
}
