package com.wangyh.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: wangyh-common-util
 * @description:
 * @author: Wangyh
 * @create: 2021-11-20 00:36
 **/
@Slf4j
public class RedisUtil {

    //address of your redis server
    private static final String redisHost = "cloud";
    private static final Integer redisPort = 6379;

    //the jedis connection pool..
    private static JedisPool pool = null;

    public RedisUtil() {
        //configure our pool connection
        pool = new JedisPool(redisHost, redisPort);

    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public void expire(String key, long seconds) {
        try (Jedis jedis = pool.getResource()) {
            jedis.expire(key, seconds);
        }
    }


}