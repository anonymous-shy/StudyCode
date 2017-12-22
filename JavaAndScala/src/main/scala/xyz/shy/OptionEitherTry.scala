package xyz.shy

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Created by Shy on 2017/12/22
  *
  * Option: 解决null（空指针）问题
  * Either: 解决返回值不确定（返回两个值的其中一个）问题
  * Try: 解决函数可能会抛出异常问题
  */

object OptionEitherTry {

  def main(args: Array[String]): Unit = {
    //在Scala的集合类中使用Option
    val bag = List("1", "2", "foo", "4", "bar")
    //通过flatMap将原来的Option对象列表转换为整数列表
    //由于Option是一个含有一个元素或0个元素(None)的集合，故能做出该转换
    println(bag.flatMap(toInt))
    //通过collect方法实现同样的功能
    println(bag.map(toInt).collect { case Some(i) => i })

    val value1 = Some("AnonYmous")
    val value2 = None
    printContentLength(value1) //length： 6
    printContentLength(value2) //无打印

    val name1 = Some("  name  ")
    val name2 = None
    println(trimUpper(name1)) //Some(NAME)
    println(trimUpper(name2)) //None

    println(divideBy(1, 1).getOrElse(0)) // 1
    println(divideBy(1, 0).getOrElse(0)) //0
    divideBy(1, 1).foreach(println) // 1
    divideBy(1, 0).foreach(println) // no print

    divideBy(1, 0) match {
      case Success(i) => println(s"Success, value is: $i")
      case Failure(s) => println(s"Failed, message is: $s")
    }

    val filename = "/etc/passwd"
    readTextFile(filename) match {
      case Success(lines) => lines.foreach(println)
      case Failure(f) => println(f)
    }

    divideBy2(1, 0) match {
      case Left(s) => println("Answer: " + s)
      case Right(i) => println("Answer: " + i)
    }
  }

  //Option//////////////////////////////////////////
  /**
    * Option实际上有3个类型：Option、Some和None，
    * Some和None都是Option的子类型，Some和None。
    * Option表示可选的值，它的返回类型是scala.Some或scala.None。
    * Some代表返回有效数据，None代表返回空值。
    *
    * @param s str
    * @return
    */
  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }

  //Option的高阶函数
  def printContentLength(x: Option[String]): Unit = {
    x.map("length: " + _.length).foreach(println)
  }

  def trimUpper(x: Option[String]): Option[String] = {
    x map (_.trim) filter (!_.isEmpty) map (_.toUpperCase)
  }

  //Try/Success/Failure

  def divideBy(x: Int, y: Int): Try[Int] = {
    Try(x / y)
  }

  def readTextFile(filename: String): Try[List[String]] = {
    Try(Source.fromFile(filename).getLines.toList)
  }

  //Either/Left/Right
  def divideBy2(x: Int, y: Int): Either[String, Int] = {
    if(y == 0) Left("Dude, can't divide by 0")
    else Right(x / y)
  }
}
