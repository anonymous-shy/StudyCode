package xyz.shy.streaming

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
  * Created by Shy on 2019/1/19
  * From Collection To Redis
  */

object StreamingFromCollection2Redis {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val data = env.fromCollection(1 to 20)
    val num = data.map(i => ("L_FlinkSinkRedis", s"s$i"))
    val config = new FlinkJedisPoolConfig.Builder().setHost("192.168.71.63").setPort(7003).build()
    val redisSink = new RedisSink[(String, String)](config, new MyRedisMapper)
    num.rebalance.print().setParallelism(1)
    num.addSink(redisSink)
    env.execute()
  }

  class MyRedisMapper extends RedisMapper[(String, String)] {
    override def getCommandDescription: RedisCommandDescription = {
      new RedisCommandDescription(RedisCommand.LPUSH)
    }

    override def getValueFromData(t: (String, String)): String = t._2

    override def getKeyFromData(t: (String, String)): String = t._1
  }

}
