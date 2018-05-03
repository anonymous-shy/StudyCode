package xyz.shy

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Shy on 2018/5/2
  */

object SeqDemo extends App {

  val ab = ArrayBuffer[String]()
  ab += "SHY"
  ab.append("EMMA")
  println(ab)
}
