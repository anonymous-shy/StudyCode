package xyz.shy

import scala.math._

/**
  * Created by Shy on 2017/10/10
  * 高阶函数
  */

object Chapter12Demo extends App {

  /**
    * 1.作为值的函数
    * ceil _ 表示 _将ceil方法转为函数
    * fun是一个包含函数的变量
    */
  val num = 3.14
  val fun = ceil _
  println(fun(num))
  Array(3.14, 1.42, 2.0).map(fun).foreach(println)

  /**
    * 2.匿名函数
    */
  val triple = (x: Double) => 3 * x // def triple(x: Double) = 3 * x

  /**
    * 3.带函数参数的函数(高阶函数)
    */
  val fun1 = 3 * (_: Double)

  /**
    * 6.闭包
    */
  def mulBy(factor: Double) = (x: Double) => factor * x

  val t = mulBy(3)
  val h = mulBy(0.5)

  println(t(14), h(14))

  /**
    * 8.柯里化
    */
  def mul1(x: Int, y: Int) = x * y

  def mul2(x: Int) = (y: Int) => x * y

  def mul3(x: Int)(y: Int) = x * y
}
