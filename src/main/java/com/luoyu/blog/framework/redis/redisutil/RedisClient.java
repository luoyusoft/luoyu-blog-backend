package com.luoyu.blog.framework.redis.redisutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description RedisClient
 */
@Component
@Slf4j
public class RedisClient {

    /**
     * @author jinhaoxun
     * @description invoke方法
     * @param pool
     * @param clients
     * @return T
     */
    public <T> T invoke(JedisPool pool, RedisClientInvoker<T> clients) {
        T obj = null;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            obj = clients.invoke(jedis);
        } catch (JedisException | IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
        return obj;
    }
}