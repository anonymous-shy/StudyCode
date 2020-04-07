package xyz.shy.streaming

import org.apache.flink.api.common.functions.{FilterFunction, RichMapFunction}

object TransformDemo {

	def main(args: Array[String]): Unit = {

	}
}

class MyFilter extends FilterFunction[String] {
	override def filter(t: String): Boolean = {
		t.endsWith("S")
	}
}

class MyRichMap extends RichMapFunction[String, String] {
	override def map(in: String): String = {
		val out = in
		out
	}
}
