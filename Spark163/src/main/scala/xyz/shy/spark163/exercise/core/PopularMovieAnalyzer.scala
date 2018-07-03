package xyz.shy.spark163.exercise.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/6/9.
  * 年龄段在“18-24”的男性年轻人，最喜欢看哪10部电影
  */
object PopularMovieAnalyzer {

  def main(args: Array[String]): Unit = {
    // 第一个参数传入运行模式
    var master = if (args.length > 0) args(0) else "local[*]"
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName("PopularMovieAnalyzer")
    val sc = new SparkContext(conf)

    val DATA_PATH = "hdfs://tagticHA/user/shy/data/ml-1m/"
    val usersRdd = sc.textFile(DATA_PATH + "users.dat")
    val moviesRdd = sc.textFile(DATA_PATH + "movies.dat")
    val ratingsRdd = sc.textFile(DATA_PATH + "ratings.dat")
    val USER_AGE = "18"
    //users: RDD[(userID, age)]
    val users = usersRdd.map(_.split("::")).map { x =>
      (x(0), x(2))
    }.filter(_._2 == USER_AGE)

    //broadcast distinct user
    val userList = users.map(_._1).distinct.collect
    val broadcastUserList = sc.broadcast(userList)

    /**
      * Step 3: map-side join RDDs
      */
    val topKmovies = ratingsRdd.map(_.split("::")).map(x => (x(0), x(1)))
      .filter(x => broadcastUserList.value.contains(x._1))
      .map(x => (x._2, 1))
      .reduceByKey(_ + _)
      .map(x => (x._2, x._1))
      .sortByKey(false)
      .map(x => (x._2, x._1))
      .take(10)

    /**
      * Transfrom filmID to fileName
      */
    val movieID2Name = moviesRdd.map(_.split("::")).map { x =>
      (x(0), x(1))
    }.collect().toMap

    topKmovies.map(x => (movieID2Name.getOrElse(x._1, null), x._2)).foreach(println)
    sc.stop()
  }
}
