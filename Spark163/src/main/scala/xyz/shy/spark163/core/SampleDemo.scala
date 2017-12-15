package xyz.shy.spark163.core

import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

/**
  * Created by Shy on 2017/3/23.
  */
object SampleDemo extends App {

  val conf = new SparkConf()
    .setAppName("SampleDemo")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)

  private val data = sc.makeRDD(Range(1, 100), 4)
  /**
    * withReplacement : scala.Boolean, 是否放回取样 true表示有放回 false表示无放回
    * fraction : scala.Double,  取样比例
    * seed : scala.Long = { /* compiled code */ } 随机种子 new Random().nextInt()
    */
  data.sample(false, 0.1, new Random().nextInt()).collect().foreach(println(_))
  println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
  //num : size of the returned sample
  data.takeSample(false, 10, new Random().nextInt()).foreach(println(_))
}
