package com.jinhx.blog.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * RedisUtils
 * @author jinhx
 * @date 2018/10/19 21:51
 * @description RedisUtils
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    // 默认过期时长，单位：毫秒
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24 * 1000;

    // 不设置过期时长
    public final static long NOT_EXPIRE = -1;

    /**
     * 设置值与过期时间
     * @param key key
     * @param value value
     * @param expire expire
     */
    public void set(String key, Object value, long expire) {
        valueOperations.set(key, JsonUtils.objectToJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 设置值，默认过期时间1天
     * @param key key
     * @param value value
     */
    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 获取对象，同时设置过期时间
     * @param key key
     * @param clazz clazz
     * @param expire expire
     * @param <T> <T>
     * @return 对象
     */
    public <T> T getObj(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
        }
        return value == null ? null : JsonUtils.jsonToObject(value, clazz);
    }

    /**
     * 获取对象，不设置过期时间
     * @param key key
     * @param clazz clazz
     * @param <T> <T>
     * @return 对象
     */
    public <T> T getObj(String key, Class<T> clazz) {
        return getObj(key, clazz, NOT_EXPIRE);
    }

    /**
     * 获取值，同时设置过期时间
     * @param key key
     * @param expire expire
     * @return 值
     */
    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
        }
        return value;
    }

    /**
     * 获取值，不设置过期时间
     * @param key key
     * @return 值
     */
    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    /**
     * 删除
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 更新过期时间
     * @param key key
     */
    public void updateExpire(String key) {
        redisTemplate.expire(key, DEFAULT_EXPIRE, TimeUnit.MILLISECONDS);
    }

    /**
     * 如果key不存在就设置value值，并返回true
     * 如果key存在则不进行操作，并返回false
     * @param key key
     * @param value value
     * @param expire expire
     * @return 是否成功
     */
    public Boolean setIfAbsent(String key, String value, long expire) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.MILLISECONDS);
    }

}
