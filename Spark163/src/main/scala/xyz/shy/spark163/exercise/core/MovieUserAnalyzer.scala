package xyz.shy.spark163.exercise.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2017/6/9.
  * 看过“Lord of the Rings, The (1978)”用户年龄和性别分布
  * TODO : 以及看过每部电影的用户年龄和性别分布
  */
object MovieUserAnalyzer {

  def main(args: Array[String]): Unit = {
    // 第一个参数传入运行模式
    var master = if (args.length > 0) args(0) else "local[*]"
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName("MovieUserAnalyzer")
    val sc = new SparkContext(conf)

    /**
      * Step 1: Create RDDs
      */
    val DATA_PATH = "hdfs://tagticHA/user/shy/data/ml-1m/"
    val MOVIE_TITLE = "Lord of the Rings, The (1978)"
    val MOVIE_ID = "2116"
    val usersRdd = sc.textFile(DATA_PATH + "users.dat")
    val ratingsRdd = sc.textFile(DATA_PATH + "ratings.dat")

    /**
      * Step 2: Extract columns from RDDs
      * users.dat   UserID::Gender::Age::Occupation::Zip-code
      * movies.dat  MovieID::Title::Genres
      */
    //users: RDD[(userID, (gender, age))]
    val users = usersRdd.map(_.split("::")).map(x => (x(0), (x(1), x(2))))
    val rating = ratingsRdd.map(_.split("::")).map(x => (x(0), x(1)))
    //usermovie: RDD[(userID, movieID)]
    val usermovie = rating.filter(_._2 == MOVIE_ID)

    /**
      * Step 3: join RDDs
      */
    //useRating: RDD[(userID, (movieID, (gender, age))]
    val userRating = usermovie.join(users)
    //userRating.take(1).foreach(print)

    //movieuser: RDD[(movieID, (movieTile, (gender, age))]
    val userDistribution = userRating.map { x =>
      (x._2._2, 1)
    }.reduceByKey(_ + _)
    userDistribution.foreach(println)

    sc.stop()
  }
}
