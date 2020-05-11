package com.jinhaoxun.blog.framework.tt.redis.jedisutil;

import com.jinhaoxun.blog.common.exception.CustomRuntimeException;
import com.jinhaoxun.blog.common.response.ResponseMsg;
import com.jinhaoxun.blog.common.util.datautil.SerializerUtil;
import com.jinhaoxun.blog.common.util.datautil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Jedis工具类(推荐存Byte数组，存Json字符串效率更慢)
 */
@Slf4j
@Component
public class JedisUtil {

    /**
     * 自定义状态OK
     */
    public final static String OK = "OK";

    /**
     * 静态注入JedisPool连接池
     * 本来是正常注入JedisUtil，可以在Controller和Service层使用，但是重写Shiro的CustomCache无法注入JedisUtil
     * 现在改为静态注入JedisPool连接池，JedisUtil直接调用静态方法即可
     * https://blog.csdn.net/W_Z_W_888/article/details/79979103
     */
    private static JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        JedisUtil.jedisPool = jedisPool;
    }

    /**
     * @author jinhaoxun
     * @description 获取Jedis实例方法
     * @return Jedis
     * @throws CustomRuntimeException
     */
    public static synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","获取Jedis资源异常:" + e.getMessage());
        }
    }

    /**
     * @author jinhaoxun
     * @description 释放Jedis资源方法
     * @throws CustomRuntimeException
     */
    public static void closePool() {
        try {
            jedisPool.close();
        }catch (Exception e){
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","释放Jedis资源异常:" + e.getMessage());
        }
    }

    /**
     * @author jinhaoxun
     * @description 获取redis键值-object方法
     * @return Object
     * @throws CustomRuntimeException
     */
    public static <T> T getObject(String key,Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if(StringUtil.isNotNull(bytes)) {
                return SerializerUtil.deserialize(bytes,clazz);
            }
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","获取Redis键值getObject方法异常:key=" + key
                    + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * @author jinhaoxun
     * @description 设置redis键值-object方法
     * @param key 设置的key
     * @param value 设置的value
     * @return String
     * @throws CustomRuntimeException
     */
    public static String setObject(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key.getBytes(), SerializerUtil.serialize(value));
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","设置Redis键值setObject方法异常:key=" + key
                    + " value=" + value + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 设置redis键值-object-expiretime方法
     * @param key 设置的key
     * @param value 设置的value
     * @param expiretime 过期时间
     * @return String
     * @throws CustomRuntimeException
     */
    public static String setObject(String key, Object value, int expiretime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(key.getBytes(), SerializerUtil.serialize(value));
            if(OK.equals(result)) {
                jedis.expire(key.getBytes(), expiretime);
            }
            return result;
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","设置Redis键值setObject方法异常:key=" + key
                    + " value=" + value + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 获取redis键值-Json方法
     * @param key 设置的key
     * @return String
     * @throws CustomRuntimeException
     */
    public static String getJson(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","获取Redis键值getJson方法异常:key=" + key
                    + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 设置redis键值-Json方法
     * @param key 设置的key
     * @param value 设置的value
     * @return String
     * @throws CustomRuntimeException
     */
    public static String setJson(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","设置Redis键值setJson方法异常:key=" + key
                    + " value=" + value + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 设置redis键值-Json-expiretime方法
     * @param key 设置的key
     * @param value 设置的value
     * @param expiretime 过期时间
     * @return String
     * @throws CustomRuntimeException
     */
    public static String setJson(String key, String value, int expiretime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(key, value);
            if(OK.equals(result)) {
                jedis.expire(key, expiretime);
            }
            return result;
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","设置Redis键值setJson方法异常:key=" + key
                    + " value=" + value + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 删除key方法
     * @param key 删除的key
     * @return Long
     * @throws CustomRuntimeException
     */
    public static Long delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key.getBytes());
        }catch(Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","删除Redis的键delKey方法异常:key=" + key
                    + " cause=" + e.getMessage());
        }finally{
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 判断key是否存在方法
     * @param key 判断的key
     * @return Boolean
     * @throws CustomRuntimeException
     */
    public static Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key.getBytes());
        }catch(Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","查询Redis的键是否存在exists方法异常:key=" + key
                    + " cause=" + e.getMessage());
        }finally{
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 模糊查询获取key集合方法
     * @param key 模糊查询的key
     * @return Set<String>
     * @throws CustomRuntimeException
     */
    public static Set<String> keysStringSet(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(key);
        }catch(Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","模糊查询Redis的键集合keysS方法异常:key=" + key
                    + " cause=" + e.getMessage());
        }finally{
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 模糊查询获取key集合方法
     * @param key 模糊查询的key
     * @return Set<byte[]>
     * @throws CustomRuntimeException
     */
    public static Set<byte[]> keysByteSet(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(key.getBytes());
        }catch(Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","模糊查询Redis的键集合keysB方法异常:key=" + key
                    + " cause=" + e.getMessage());
        }finally{
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 获取过期剩余时间方法
     * @param key 查询的key
     * @return Long
     * @throws CustomRuntimeException
     */
    public static Long getExpireTime(String key) {
        Long result = -2L;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.ttl(key);
            return result;
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.JEDIS_UTIL_WRONG.getCode(),
                    "系统异常，请稍后重试！","获取Redis键过期剩余时间getExpireTime方法异常:key=" + key
                    + " cause=" + e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }
}
