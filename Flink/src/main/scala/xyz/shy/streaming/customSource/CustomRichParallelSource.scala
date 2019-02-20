package xyz.shy.streaming.customSource

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

/**
  * Created by Shy on 2019/2/20
  * RichParallelSourceFunction 会额外提供open和close方法
  * 针对source中如果需要获取其他链接资源，那么可以在open方法中获取资源链接，在close中关闭资源链接
  */

class CustomRichParallelSource extends RichParallelSourceFunction[Long] {
  var cnt = 1
  var isRunning = true

  override def run(sourceContext: SourceFunction.SourceContext[Long]): Unit = {
    while (isRunning) {
      sourceContext.collect(cnt)
      cnt += 1
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }

  /**
    * 这个方法只会在最开始的时候被调用一次
    * 实现获取链接的代码
    *
    * @param parameters
    */
  override def open(parameters: Configuration): Unit = super.open(parameters)

  /**
    * 实现关闭链接的代码
    */
  override def close(): Unit = super.close()
}
