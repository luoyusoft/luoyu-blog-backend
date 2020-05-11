package com.jinhaoxun.blog.framework.tt.redis.redisutil;

import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description RedisClientInvoker接口
 */
public interface RedisClientInvoker<T> {

    T invoke(Jedis jedis) throws IOException;

}