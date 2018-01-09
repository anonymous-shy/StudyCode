package xyz.shy

import util.control.Breaks._

/**
  * Created by Shy on 2018/1/9
  */

object BreakAndContinueDemo extends App {

  println("\n ===== Break Example")
  breakable {
    for (i <- Range(1, 10)) {
      println(i)
      if (i > 4) break // break out of the loop
    }
  }

  println("\n ===== Continue Example")
  val s = "Spark’s Standalone Mode cluster manager"
  var nums = 0
  for (i <- 0 until s.length) {
    breakable {
      if (s.charAt(i) != "S")
        break
      else nums += 1
    }
  }
  println(s"nums = $nums")
}
