package xyz.shy

/**
  * Created by Shy on 2018/12/3
  * Scala 工具方法
  */
//case class ParseOp[T](op: String => T)

object CommonUtils {

  /**
    * 1.Scala类型转换
    *
    * @param s string
    * @tparam T class type
    * @return option
    */
  def parse[T: ParseOp](s: String): Option[T] = try {
    Some(implicitly[ParseOp[T]].op(s))
  } catch {
    case _ => None
  }

  case class ParseOp[T](op: String => T)

  implicit val popDouble = ParseOp[Double](_.toDouble)
  implicit val popInt = ParseOp[Int](_.toInt)
  implicit val popLong = ParseOp[Long](_.toLong)
  implicit val popFloat = ParseOp[Float](_.toFloat)


  def main(args: Array[String]): Unit = {
    val dd02 = "0.234"
    println(dd02.isInstanceOf[String]) // 判断是否为String类型
    println(parse[Double](dd02).get) // 正确的转换方式 println(dd02.asInstanceOf[Double]) // 错误的转换方式：强制类型转换：java.lang.String cannot be cast to java.lang.Double println(dd02.toFloat) // 错误的转换方式，会抛出异常： java.lang.String cannot be cast to java.lang.Double
//    println(dd02.asInstanceOf[Double]) // 错误的转换方式：强制类型转换：java.lang.String cannot be cast to java.lang.Double
    println(dd02.toDouble) // 错误的转换方式，会抛出异常：  java.lang.String cannot be cast to java.lang.Double
  }
}
