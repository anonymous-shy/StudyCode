package xyz.shy.batch

import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

/**
	* Created by Shy on 2019/1/22
	*/

object CounterDemo {

	def main(args: Array[String]): Unit = {
		val env = ExecutionEnvironment.getExecutionEnvironment
		import org.apache.flink.api.scala._
		val broadData = env.fromCollection(List((1, "Fender"), (3, "Gibson"), (5, "MusicMan")))
		val data = env.fromCollection(1 to 20)
		val r: Range = 1 to 20
		val data1 = env.fromElements("Fender", "Gibson", "MusicMan")

		data1.map(new RichMapFunction[String, String] {
			override def open(parameters: Configuration): Unit = {
				super.open(parameters)
			}

			override def map(value: String) = ???
		})

		val res = data.map(new RichMapFunction[Int, Int] {
			// 1. 定义累加器
			val cnt = new IntCounter()

			override def open(parameters: Configuration): Unit = {
				super.open(parameters)
				// 2. 注册累加器
				getRuntimeContext.addAccumulator("Counter", this.cnt)
			}

			override def map(in: Int) = {
				// 3.使用累加器
				this.cnt.add(1)
				println(in)
				in
			}
		})
		res.writeAsText("./count.txt")
		val jobResult = env.execute(getClass.getSimpleName)
		val num = jobResult.getAccumulatorResult[Int]("Counter")
		println(s"num: $num")
	}
}
