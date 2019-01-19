package xyz.shy.spark220.StructuredStreaming

import org.apache.spark.sql.SparkSession

/**
  * Created by Shy on 2019/1/5
  */

object StructuredNetwordWC {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName(getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    val lines = spark.readStream
      .format("socket")
      .option("host", "tagtic-slave01")
      .option("port", "9999")
      .load()

    val words = lines.as[String].flatMap(_.split(" "))

    val wordCounts = words.groupBy("value").count()

    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()
    query.awaitTermination()
  }
}
