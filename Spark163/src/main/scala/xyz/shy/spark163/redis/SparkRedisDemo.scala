package xyz.shy.spark163.redis

import com.redislabs.provider.redis._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2018/5/30
  * Api: https://github.com/RedisLabs/spark-redis
  */

object SparkRedisDemo extends App {

  val conf = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[*]")
    // initial redis host - can be any node in cluster mode
    .set("redis.host", "tagtic-slave02")
    // initial redis port
    .set("redis.port", "7002")
  val sc = new SparkContext(conf)
  private val list: RDD[String] = sc.parallelize(Seq("Shy", "Emma", "Taylor", "ReAct", "Dolores", "AnonYmous"))
  /* spark-redis-0.5.1
  val keysRDD = sc.fromRedisKeyPattern(("tagtic-slave01", 7000), "user", 2)
  private val hashRdd: RDD[(String, String)] = keysRDD.getHash()
  //  private val listRdd: RDD[String] = keysRDD.getList()
  //  private val kvRdd: RDD[(String, String)] = keysRDD.getKV()
  hashRdd.collect.foreach(println)
  sc.toRedisLIST(list, "names", ("tagtic-slave02", 7002))*/
  private val listRdd: RDD[String] = sc.fromRedisList[String]("names")
  listRdd.cache
  listRdd.collect.foreach(println)
  private val kvRdd: RDD[(String, String)] = listRdd.map((_, 1.toString))
  sc.toRedisHASH(kvRdd, "bar")
}
