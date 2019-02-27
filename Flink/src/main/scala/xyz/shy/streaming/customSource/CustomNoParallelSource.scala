package xyz.shy.streaming.customSource

import org.apache.flink.streaming.api.functions.source.SourceFunction

/**
  * Created by Shy on 2019/2/20
  */

class CustomNoParallelSource extends SourceFunction[Long] {
  var cnt = 1
  var isRunning = true

  /**
    * 主要的方法
    * 启动一个source
    * 大部分情况下，都需要在这个run方法中实现一个循环，这样就可以循环产生数据了
    *
    * @param sourceContext
    */
  override def run(sourceContext: SourceFunction.SourceContext[Long]): Unit = {
    while (isRunning) {
      sourceContext.collect(cnt)
      cnt += 1
      Thread.sleep(1000)
    }
  }

  /**
    * 取消一个cancel的时候会调用的方法
    */
  override def cancel(): Unit = {
    isRunning = false
  }
}
