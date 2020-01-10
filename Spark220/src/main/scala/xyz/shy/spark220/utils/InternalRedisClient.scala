package xyz.shy.spark220.utils

import redis.clients.jedis.{JedisPool, JedisPoolConfig}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

/**
  * @author Shy
  */
/**
  * Internal Redis client for managing Redis connection based on RedisPool
  */
object InternalRedisClient extends Serializable {

  @transient private var pool: JedisPool = _

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
               maxTotal: Int, maxIdle: Int, minIdle: Int): Unit = {
    makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle, testOnBorrow = true, testOnReturn = false, 10000)
  }

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
               maxTotal: Int, maxIdle: Int, minIdle: Int, testOnBorrow: Boolean,
               testOnReturn: Boolean, maxWaitMillis: Long): Unit = {
    if (pool == null) {
      val poolConfig = new JedisPoolConfig()
      poolConfig.setMaxTotal(maxTotal)
      poolConfig.setMaxIdle(maxIdle)
      poolConfig.setMinIdle(minIdle)
      poolConfig.setTestOnBorrow(testOnBorrow)
      poolConfig.setTestOnReturn(testOnReturn)
      poolConfig.setMaxWaitMillis(maxWaitMillis)
      pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout)

      val hook = new Thread {
        override def run(): Unit = pool.destroy()
      }
      sys.addShutdownHook(hook.run())
    }
  }

  def getPool: JedisPool = {
    assert(pool != null)
    pool
  }

  /*def apply(): JedisPool = {
    if (pool == null) {
      this.synchronized {
        if (pool == null) {
          val poolConfig = new JedisPoolConfig()
          poolConfig.setMaxTotal(5000)
          poolConfig.setMinIdle(10)
          poolConfig.setMaxIdle(30)
          poolConfig.setMaxWaitMillis(100 * 1000)
          poolConfig.setBlockWhenExhausted(true)
          pool = new JedisPool(poolConfig, "spark003", 6379, 6000, "123456")
        }
      }
    }
    pool
  }*/

}
