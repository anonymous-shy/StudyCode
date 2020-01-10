package xyz.shy.spark220.utils;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisClusterPool {

    private static ThreadLocal<ShardedJedis> jedisLocal = new ThreadLocal<ShardedJedis>();
    private static ShardedJedisPool pool;

    static {
        pool = JedisClusterPoolFactory.getShardedJedisPool();
    }

    public ShardedJedis getClient() {
        ShardedJedis jedis = jedisLocal.get();
        if (jedis == null) {
            jedis = pool.getResource();
            jedisLocal.set(jedis);
        }
        return jedis;
    }

    //关闭连接
    public void returnResource() {
        ShardedJedis jedis = jedisLocal.get();
        if (jedis != null) {
            pool.destroy();
            jedisLocal.set(null);
        }
    }

    public static void main(String[] args) {
        JedisClusterPool jedisClusterPool = new JedisClusterPool();
        ShardedJedis client = jedisClusterPool.getClient();
        String foo = client.get("foo");
        System.out.println("K foo is " + foo);
    }
}
