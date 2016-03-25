package com.lqipr.core.util;


import com.lqipr.core.redis.RedisPool;

import java.util.HashMap;
import java.util.Properties;

/**
 * redis连接池 Map对象
 */
public class RedisUtil {

    /**
     * redis连接池Map
     * 可以有多个连接池对象
     */
    private static HashMap<String, RedisPool> redisPoolMap = new HashMap<String, RedisPool>();


    private final static String REDIS_PROFILE = "properties/redis.properties";

    /**
     * 获取连接池对象
     *
     * @param poolName 连接池的名称 需要和配置文件对应
     * @return 连接池对象
     */
    public static synchronized RedisPool getRedisPool(String poolName) {
        RedisPool rp = null;
        if (redisPoolMap.containsKey(poolName)) {
            rp = redisPoolMap.get(poolName);
        } else {
            //读取poolname对应的配置文件的属性来构建poolName
            try {
                Properties pro = PropertiesUtil.getProperties(REDIS_PROFILE);
                rp = createRedisPool(poolName, pro);
                redisPoolMap.put(poolName, rp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rp;
    }

    private static RedisPool createRedisPool(String poolName, Properties pro) {
        String ip = pro.getProperty(poolName + ".redis.pool.ip");
        String port = pro.getProperty(poolName + ".redis.pool.port");

        int maxWait = StringUtil.convert(pro.getProperty(poolName + ".redis.pool.maxWait"), 0);
        int maxActive = StringUtil.convert(pro.getProperty(poolName + ".redis.pool.maxActive"), 0);
        int maxIdle = StringUtil.convert(pro.getProperty(poolName + ".redis.pool.maxIdle"), 0);
        System.out.println((poolName + ".redis.pool.ip") + ":[ip:" + ip + " 端口:" + port + " 等待:" + maxWait + " maxActive:" + maxActive + " maxIdle:" + maxIdle + "]");
        return new RedisPool(poolName, ip, port, maxWait, maxActive, maxIdle);
    }
}
