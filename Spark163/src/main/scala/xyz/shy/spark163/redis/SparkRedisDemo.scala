package xyz.shy.spark163.redis

import com.redislabs.provider.redis._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Shy on 2018/5/30
  */

object SparkRedisDemo extends App {

  val conf = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  sc.fromRedisKeyPattern(("tagtic-slave01", 7000), "*", 5)
}
