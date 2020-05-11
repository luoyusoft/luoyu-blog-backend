package com.jinhaoxun.blog.framework.tt.shiro.cache;

import com.alibaba.fastjson.JSONObject;
import com.jinhaoxun.accommon.constant.AbstractConstant;
import com.jinhaoxun.blog.common.util.otherutil.JwtUtil;
import com.jinhaoxun.blog.framework.tt.redis.jedisutil.JedisUtil;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import com.jinhaoxun.blog.common.util.datautil.SerializerUtil;

import java.util.*;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 重写 Shiro 的 Cache 保存读取
 */
public class CustomCache<K,V> implements Cache<K,V> {

    /**
     * @author jinhaoxun
     * @description 缓存的 key 名称获取为 shiro:userId
     * @param key 缓存的 key
     * @return  String 获取 key 的内容
     */
    private String getKey(Object key){
        return AbstractConstant.SHIRO_ROLE_PERMISSION_KEY + JwtUtil.getClaim(key.toString(), AbstractConstant.TOKEN_USER_ID);
    }

    /**
     * @author jinhaoxun
     * @description 获取缓存
     * @param key 缓存的 key
     * @return Object 获取 key 的内容
     * @throws CacheException
     */
    @Override
    public Object get(Object key) throws CacheException {
        if(!JedisUtil.exists(this.getKey(key))){
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(JedisUtil.getObject(this.getKey(key),String.class));
        AuthorizationInfo authorizationInfo = JSONObject.toJavaObject(jsonObject,AuthorizationInfo.class);
        return authorizationInfo;
    }

    /**
     * @author jinhaoxun
     * @description 保存缓存
     * @param key 保存的 key
     * @param value 保存的 value
     * @return Object
     * @throws CacheException
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 设置 Redis 的 Shiro 缓存
        return JedisUtil.setObject(this.getKey(key), value, AbstractConstant.SHIRO_ROLE_PERMISSION_EXPIRATION_TIME);
    }

    /**
     * @author jinhaoxun
     * @description 移除缓存
     * @param key 移除的 key
     * @return Object
     * @throws CacheException
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if(!JedisUtil.exists(this.getKey(key))){
            return null;
        }
        JedisUtil.delKey(this.getKey(key));
        return null;
    }

    /**
     * @author jinhaoxun
     * @description 清空所有缓存
     * @throws CacheException
     */
    @Override
    public void clear() throws CacheException {
        JedisUtil.getJedis().flushDB();
    }

    /**
     * @author jinhaoxun
     * @description 获取缓存的个数
     * @return int 缓存的个数
     */
    @Override
    public int size() {
        Long size = JedisUtil.getJedis().dbSize();
        return size.intValue();
    }

    /**
     * @author jinhaoxun
     * @description 获取所有的 key
     * @return Set 所有的 key
     */
    @Override
    public Set keys() {
        Set<byte[]> keys = JedisUtil.getJedis().keys(("*").getBytes());
        Set<Object> set = new HashSet<Object>();
        for (byte[] bs : keys) {
            set.add(SerializerUtil.deserialize(bs));
        }
        return set;
    }

    /**
     * @author jinhaoxun
     * @description 获取所有的 value
     * @return Collection 所有的 value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            JSONObject jsonObject = JSONObject.parseObject(JedisUtil.getObject(this.getKey(key),String.class));
            AuthorizationInfo authorizationInfo = JSONObject.toJavaObject(jsonObject,AuthorizationInfo.class);
            values.add(authorizationInfo);
        }
        return values;
    }
}
