package com.scorpios.redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig,"192.168.43.86",6379);
    }

    public static Jedis getJedis() throws Exception{
        if(null != jedisPool){
            return jedisPool.getResource();
        }
        throw new Exception("JedisPool is not ok");
    }

}
