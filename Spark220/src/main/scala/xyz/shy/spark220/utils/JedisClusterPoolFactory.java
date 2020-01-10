package xyz.shy.spark220.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 基于ShardedJedisPool实现。3.0后使用cluster!!!
 * !!! 基本没有了！！！
 */
public class JedisClusterPoolFactory {

    //JedisPool连一台Redis，ShardedJedisPool连Redis集群，通过一致性哈希算法决定把数据存到哪台上，算是一种客户端负载均衡，
    private static ShardedJedisPool pool;

    //静态代码初始化连接池配置
    static {
        try {
            //初始化连接池参数配置
            JedisPoolConfig config = initConfig();
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
//            String host = "localhost:6379:testredis,localhost:6380:testredis,localhost:6381:testredis";//服务器地址,密码
            String host = "192.168.71.63:7002,192.168.71.63:7003,192.168.71.64:7005";//服务器地址,密码
            Set<String> hosts = initHost(host);
            for (String hs : hosts) {
                String[] values = hs.split(":");
                JedisShardInfo shard = new JedisShardInfo(values[0], Integer.parseInt(values[1]));
                if (values.length > 2) {
                    shard.setPassword(values[2]);
                }
                shards.add(shard);
            }
            pool = new ShardedJedisPool(config, shards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化连接池参数
    private static JedisPoolConfig initConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        int MAXTOTAL = 512;
        config.setMaxTotal(MAXTOTAL);
        int MAXIDLE = 256;
        config.setMaxIdle(MAXIDLE);
        int MINIDEL = 16;
        config.setMinIdle(MINIDEL);
        int MAXWAIRMILLIS = 1000;
        config.setMaxWaitMillis(MAXWAIRMILLIS);
        boolean TESTONBORROW = true;
        config.setTestOnBorrow(TESTONBORROW);
        boolean TESTONRETURN = false;
        config.setTestOnReturn(TESTONRETURN);
        boolean TESTWHILEIDLE = false;
        config.setTestWhileIdle(TESTWHILEIDLE);
        return config;
    }

    private static Set<String> initHost(String values) {
        if (StringUtils.isBlank(values)) {
            throw new NullPointerException("redis host not found");
        }
        String[] sentinelArray = values.split(",");
        return new HashSet<String>(Arrays.asList(sentinelArray));
    }

    public static ShardedJedisPool getShardedJedisPool() {
        return pool;
    }
}
