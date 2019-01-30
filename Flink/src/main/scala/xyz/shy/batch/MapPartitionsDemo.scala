package xyz.shy.batch

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

/**
  * Created by Shy on 2019/1/22
  */

object MapPartitionsDemo {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val data = env.fromCollection(List("Fender Gibson", "Air Jordan", "MusicMan PRS", "Nike AnonYmous"))
    data.mapPartition(iter => {
      // 与 spark mapPartition 一样，用于创建数据库连接，放在try-catch中
      val res = ListBuffer[String]()
      iter.foreach { w => res += w }
      res
    }).print()


  }
}
