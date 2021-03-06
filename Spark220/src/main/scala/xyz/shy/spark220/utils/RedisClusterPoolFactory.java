package xyz.shy.spark220.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;


public class RedisClusterPoolFactory implements PooledObjectFactory<JedisCluster> {
    static {
        Config config = ConfigFactory.load();
    }

    /**
     * 什么时候会调用此方法
     * 1：从资源池中获取资源的时候
     * 2：资源回收线程，回收资源的时候，根据配置的 testWhileIdle 参数
     * 判断 是否执行 factory.activateObject()方法，true 执行，false 不执行
     *
     * @param pooledObject JedisClusterPool
     * @throws Exception Ex
     */
    public void activateObject(PooledObject<JedisCluster> pooledObject) throws Exception {
        System.out.println("activate Object");
    }

    public PooledObject<JedisCluster> makeObject() throws Exception {
        System.out.println("make Object");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(256);
        // 最大空闲数
        poolConfig.setMaxIdle(32);
        // 最小空闲数
        poolConfig.setMinIdle(8);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.71.62", 7000));
        nodes.add(new HostAndPort("192.168.71.62", 7001));
        nodes.add(new HostAndPort("192.168.71.63", 7002));
        nodes.add(new HostAndPort("192.168.71.63", 7003));
        nodes.add(new HostAndPort("192.168.71.64", 7004));
        nodes.add(new HostAndPort("192.168.71.64", 7005));
//        nodes.add(new HostAndPort("192.168.71.64", 7005));
        JedisCluster JedisCluster = new JedisCluster(nodes, poolConfig);
        return new DefaultPooledObject<JedisCluster>(JedisCluster);
    }

    public void destroyObject(PooledObject<JedisCluster> pooledObject) throws Exception {
        System.out.println("destroy Object");
        JedisCluster jedisCluster = pooledObject.getObject();
        jedisCluster.close();
    }

    /**
     * 功能描述：判断资源对象是否有效，有效返回 true，无效返回 false
     *  
     * 什么时候会调用此方法
     * 1：从资源池中获取资源的时候，参数 testOnBorrow 或者 testOnCreate 中有一个 配置 为 true 时，则调用  factory.validateObject() 方法
     * 2：将资源返还给资源池的时候，参数 testOnReturn，配置为 true 时，调用此方法
     * 3：资源回收线程，回收资源的时候，参数 testWhileIdle，配置为 true 时，调用此方法
     *  
     */
    public boolean validateObject(PooledObject<JedisCluster> pooledObject) {
        System.out.println("validate Object");
        return true;
    }


    public void passivateObject(PooledObject<JedisCluster> pooledObject) throws Exception {
        System.out.println("passivate Object");
    }
}
