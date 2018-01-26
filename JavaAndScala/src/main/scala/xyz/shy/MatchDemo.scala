package xyz.shy

import java.io.{FileNotFoundException, IOException}

import scala.annotation.switch
import scala.io.Source

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

  val num = 6
  val evenOrOdd = num match {
    case 1 | 3 | 5 | 7 | 9 => println("odd")
    case 2 | 4 | 6 | 8 | 10 => println("even")
  }

  println(evenOrOdd)

  def isTrue(a: Any) = a match {
    case 0 | "" => false
    case _ => true
  }

  /*def printInfo(x: Any) = x match {
    case String => println(x)
    case Int => println(x)
    case _ => println("XXX")
  }*/



}
