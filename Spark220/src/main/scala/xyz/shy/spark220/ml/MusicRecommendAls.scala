package xyz.shy.spark220.ml

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by Shy on 2019/2/26
  * 基于 用户对艺术家 音乐播放的次数作为评价，次数越多表示对此艺术家的音乐越喜好。
  */

object MusicRecommendAls {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName(getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    val rawUserArtistRDD: RDD[String] = sc.textFile("", 8)

  }
}
