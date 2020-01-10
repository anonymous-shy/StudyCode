package xyz.shy.spark220.utils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * jediscluster从slot中取jedis就是从jedispool中取的，所以jedisCluster初始化资源池没有意义。
 * 只要保证jedisCluster作为静态方法就好。
 */
public class JedisClusterPoolUtils {

    private static JedisCluster jedisCluster = null;

    public synchronized static JedisCluster getJedisCluster() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(512);
        config.setMaxIdle(128);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);

        // 集群模式
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        // TODO 后续配置化
        nodes.add(new HostAndPort("192.168.71.62", 7000));
        nodes.add(new HostAndPort("192.168.71.62", 7001));
        nodes.add(new HostAndPort("192.168.71.63", 7002));
        nodes.add(new HostAndPort("192.168.71.63", 7003));
        nodes.add(new HostAndPort("192.168.71.64", 7004));
        nodes.add(new HostAndPort("192.168.71.65", 7005));

        // 只有当jedisCluster为空时才实例化
        if (jedisCluster == null) {
            jedisCluster = new JedisCluster(nodes, poolConfig);
        }

        return jedisCluster;
    }

    public static void closeJedisCluster(JedisCluster jedisCluster) {
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test ...
     *
     * @param args ...
     */
    public static void main(String[] args) {
        JedisCluster jedisCluster = JedisClusterPoolUtils.getJedisCluster();
        String foo = jedisCluster.get("foo");
        System.out.println(foo);
        JedisClusterPoolUtils.closeJedisCluster(jedisCluster);
    }
}
