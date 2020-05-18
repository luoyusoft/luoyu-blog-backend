package com.luoyu.blog.project.service.shiroservice;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Redis锁服务接口
 */
public interface ISyncCacheService {

    /**
     * @author jinhaoxun
     * @description 获取redis中key的锁，乐观锁实现
     * @param lockName 获取锁的名字
     * @param expireTime 锁的失效时间
     * @return Boolean 是否获取成功
     * @throws Exception
     */
    boolean getLock (String lockName, int expireTime) throws Exception;

    /**
     * @author jinhaoxun
     * @description 释放锁，直接删除key(直接删除会导致任务重复执行，所以释放锁机制设为超时20s)
     * @param lockName 释放锁的名字
     * @return Boolean 是否释放成功
     * @throws Exception
     */
    boolean releaseLock(String lockName) throws Exception;

    /**
     * @author jinhaoxun
     * @description 释放锁，直接删除key(直接删除会导致任务重复执行，所以释放锁机制设为超时20s)
     * @param lockName 释放锁的名字
     * @param releaseLockTime (单位：秒)
     * @return Boolean 是否释放成功
     * @throws Exception
     */
    boolean releaseLock(String lockName,int releaseLockTime) throws Exception;

}
