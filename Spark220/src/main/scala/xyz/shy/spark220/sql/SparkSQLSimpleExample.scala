package xyz.shy.spark220.sql

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}

/**
  * Created by Shy on 2017/6/21.
  */
object SparkSQLSimpleExample {

  case class User(userID: String, gender: String, age: String, occupation: String, zipcode: String)

  def main(args: Array[String]): Unit = {
    // 第一个参数传入运行模式
    var master = if (args.length > 0) args(0) else "local[*]"
    val spark: SparkSession = SparkSession
      .builder()
      .appName("SparkSQLSimpleExample")
      .master(master)
      //      .enableHiveSupport()
      //      .config("spark.sql.warehouse.dir", "F:\\spark-warehouse")
      .getOrCreate()
    val DATA_PATH = "hdfs://tagticHA/user/shy/data/ml-1m/"
    val userRDD = spark.sparkContext.textFile(s"${DATA_PATH}users.dat")
    val ratingsRdd = spark.sparkContext.textFile(DATA_PATH + "ratings.dat")
    /**
      * Method 1: 通过显式为RDD注入schema，将其变换为DataFrame
      */
    import spark.implicits._
    val userDF1 = userRDD
      .map(x => x.split("::"))
      .map(u => User(u(0), u(1), u(2), u(3), u(4)))
      .toDF
    //    userDF1.show()
    //    println(s"userDF.count: ${userDF1.count}")
    /**
      * Method 2: 通过反射方式，为RDD注入schema，将其变换为DataFrame
      */
    val schemaString = "userID gender age occupation zipcode"
    val fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)
    //    val userRowRDD = userRDD.mapPartitions(iter => {
    //      val list = new ListBuffer[Array[String]]()
    //      while (iter.hasNext) {
    //        list += iter.next().split("::")
    //      }
    //      list.iterator
    //    }).mapPartitions(iter => {
    //      val list = new ListBuffer[Row]()
    //      while (iter.hasNext) {
    //        list += Row(iter.next()(0), iter.next()(1).trim, iter.next()(2).trim, iter.next()(3).trim, iter.next()(4).trim)
    //      }
    //      list.iterator
    //    })
    val userRowRDD = userRDD.map(_.split("::")).map(p => Row(p(0), p(1).trim, p(2).trim, p(3).trim, p(4).trim))
    val userDF2 = spark.createDataFrame(userRowRDD, schema)
//    userDF2.show(10)
//    println(s"userDF.count: ${userDF1.count}")


    val ratingSchemaString = "userID movieID Rating Timestamp"
    val ratingSchema = StructType(ratingSchemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, true)))
    val ratingRDD = ratingsRdd.map(_.split("::")).map(p => Row(p(0), p(1).trim, p(2).trim, p(3).trim))
    val ratingDataFrame = spark.createDataFrame(ratingRDD, ratingSchema)

    /*
     *  select gender, age, count(*) as n from users group by gender, age
     */
    val mergedDataFrame = ratingDataFrame.filter("movieID = 2116").
      join(userDF2, "userID").
      select("gender", "age").
      groupBy("gender", "age").
      count
    mergedDataFrame.show()

    val mergedDataFrame2 = ratingDataFrame.filter("movieID = 2116").
      join(userDF2, userDF2("userID") === ratingDataFrame("userID"), "inner").
      select("gender", "age").
      groupBy("gender", "age").
      count
    mergedDataFrame2.show()

//    userDF2.registerTempTable("users")
    userDF2.createTempView("users")

    spark.sql("select gender, age, count(*) as n from users group by gender, age").show()

    userDF1.map { u =>
      (u.getAs[String]("userID").toLong, u.getAs[String]("age").toInt + 1)
    }.take(10).foreach(println)
  }
}
