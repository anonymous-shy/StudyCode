package xyz.shy.spark163.exercise.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/6/14.
  * 得分最高的10部电影；看过电影最多的前10个人；女性看多最多的10部电影；男性看过最多的10部电影
  */
object TopKMovieAnalyzer {

  def main(args: Array[String]): Unit = {
    // 第一个参数传入运行模式
    var master = if (args.length > 0) args(0) else "local[*]"
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName("PopularMovieAnalyzer")
    val sc = new SparkContext(conf)

    val DATA_PATH = "hdfs://tagticHA/user/shy/data/ml-1m/"
    val ratingsRdd = sc.textFile(DATA_PATH + "ratings.dat")

    //users: RDD[(userID, movieID, score)] UserID::MovieID::Rating
    val ratings = ratingsRdd.map(_.split("::")).map { x =>
      (x(0), x(1), x(2))
    }.cache

    val topKScoreMostMovie = ratings.map(x => (x._2, (x._3.toInt, 1)))
      .reduceByKey((v1, v2) => (v1._1 + v2._1, v1._2 + v2._2))
      .map(x => (x._2._1.toFloat / x._2._2.toFloat, x._1))
      .sortByKey(false)
      .take(10)
      .foreach(println)

    val topKmostPerson = ratings.map { x =>
      (x._1, 1)
    }.reduceByKey(_ + _).
      map(x => (x._2, x._1)).
      sortByKey(false).
      take(10).
      foreach(println)

    sc.stop()
  }
}
