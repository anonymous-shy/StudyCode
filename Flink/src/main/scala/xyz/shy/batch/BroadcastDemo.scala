package xyz.shy.batch

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment

/**
  * Created by Shy on 2019/1/22
  */

object BroadcastDemo {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val broadData = env.fromCollection(List((1, "Fender"), (3, "Gibson"), (5, "MusicMan")))

    val text = env.fromElements("Gibson", "Fender", "MusicMan")
//    text.map(new RichMapFunction[] {})
  }
}
