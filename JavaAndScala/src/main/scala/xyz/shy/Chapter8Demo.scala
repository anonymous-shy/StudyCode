package xyz.shy

import java.time.LocalDateTime

/**
  * Created by Shy on 2017/10/10
  * 特质 trait
  */

trait Logger {
  //抽象方法和具体方法结合在一起
  def log(msg: String)
  def info(msg: String) {log(s"INFO: $msg")}
  def warn(msg: String) {log(s"WARN: $msg")}
  def error(msg: String) {log(s"ERROR: $msg")}
}

class WebLogger extends Logger with Serializable {
  override def log(msg: String) {
    println(msg)
  }
}

trait ConsoleLogger extends Logger {
  override def log(msg: String) {
    println(msg)
  }
}

trait Timestamp extends ConsoleLogger {
  override def log(msg: String): Unit = {
    super.log(LocalDateTime.now + " " + msg)
  }
}

object ChapterDemo8 {
  //带有特质的对象
  val e = new Employee with ConsoleLogger

}
