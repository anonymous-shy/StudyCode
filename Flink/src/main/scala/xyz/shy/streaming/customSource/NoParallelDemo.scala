package xyz.shy.streaming.customSource

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Created by Shy on 2019/2/20
  */

object NoParallelDemo {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 隐式转换
    import org.apache.flink.api.scala._
    val text = env.addSource(new CustomNoParallelSource)
    text.map(line => {
      println(s"Record: $line")
      line
    }).timeWindowAll(Time.seconds(3)).sum(0)
      .print()

    env.execute(getClass.getSimpleName)
  }
}
