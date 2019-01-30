package xyz.shy.batch

import org.apache.flink.api.scala.ExecutionEnvironment

/**
  * Created by Shy on 2019/1/22
  */

object JoinDemo {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val data1 = env.fromCollection(List((1, "Fender JP"), (3, "Gibson"), (5, "MusicMan")))
    val data2 = env.fromCollection(List((1, "JP"), (3, "USA"), (5, "AnonYmous")))
    data1.join(data2)
      .where(0)
      .equalTo(0)
      .apply((first, second) => {
        (first._1, first._2, second._2)
      }).print()
  }
}
