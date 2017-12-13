package xyz.shy.spark163.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Shy on 2017/12/8.
  */
case class SearchLog(name: String, search: String, cnt: Int)

object ForeachRddWithSqlContext {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))
    val dStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      Map("bootstrap.servers" -> "192.168.1.101:9092,192.168.1.102:9092,192.168.1.103:9092"),
      Set("topic1")
    ).map(_._2)
    dStream.map((_, 1))
      .reduceByKeyAndWindow((i1: Int, i2: Int) => i1 + i2, Seconds(30), Seconds(10))
      .foreachRDD(rdd => {
        val sqlContext = SQLContextSingleton.getInstance(sc)
        import sqlContext.implicits._
        val searchLogDF = rdd.map(a => SearchLog(a._1.split("::")(0), a._1.split("::")(1), a._2)).toDF()
        searchLogDF.registerTempTable("search_log")
        sqlContext.sql(
          """
            |select name, search, sum(cnt) as ccnt
            |from search_log
            |group by name,search
            |order by ccnt desc
            |limit 3
          """.stripMargin).show()
      })
    ssc.start()
    ssc.awaitTermination()
  }
}

object SQLContextSingleton {
  @transient private var instance: SQLContext = _

  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}
