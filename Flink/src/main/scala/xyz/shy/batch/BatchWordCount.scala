package xyz.shy.batch

import org.apache.flink.api.scala.ExecutionEnvironment

/**
	* Created by Shy on 2019/1/17
	*/

object BatchWordCount {

	def main(args: Array[String]): Unit = {
		val env = ExecutionEnvironment.getExecutionEnvironment
		val textFile = env.readTextFile("")

		import org.apache.flink.api.scala._
		val wc = textFile.flatMap(_.toLowerCase.split("\\W+"))
			.filter(_.nonEmpty)
			.map((_, 1))
			.groupBy(0)
			.sum(1)
		wc.writeAsCsv("", "\n", " ").setParallelism(1)
		env.execute("Batch word count")
	}
}
