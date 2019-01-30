package xyz.shy.batch

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.FileUtils

/**
  * Created by Shy on 2019/1/22
  */

object CacheDemo {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    env.registerCachedFile("", "XXX")
    val data = env.fromCollection(1 to 20)
    data.map(new RichMapFunction[Int, Int] {
      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        val file = getRuntimeContext.getDistributedCache.getFile("XXX")
        val str = FileUtils.readFile(file, "utf8")
        println(str)
      }

      override def map(value: Int) = value
    }).print()
  }
}
