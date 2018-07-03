package xyz.shy

/**
  * Created by Shy on 2017/10/12
  * 模式匹配
  */

//abstract class Amount
//
//case class Dollar(v: Double) extends Amount
//
//case class Currency(v: Double, unit: String) extends Amount

object Chapter14Demo extends App {

  def matchTest(x: Int) = x match {
    case 1 => "O"
    case 2 => "OO"
    case _ => "OOO"
  }

  println(matchTest(2))

  //  import scala.collection.JavaConversions.propertiesAsScalaMap
  //  for ((k, v) <- System.getProperties)
  //    println(s"$k -> $v")


}
