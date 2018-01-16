package xyz.shy

import scala.annotation.switch

/**
  * Created by Shy on 2018/1/11
  */

object MatchDemo extends App {

  val i = 1
  val x = (i: @switch) match {
    case 1 => "One"
    case 2 => "Two"
    case _ => "Other"
  }
  println(x)

  //case 匹配多个条件
  val cmd = "stop"
  cmd match {
    case "start" | "go" => println("starting...")
    case "stop" | "quit" | "exit" => println("stoping...")
    case _ => println("do nothing...")
  }
}
