package com.luoyu.blog.project.service.shiroservice.impl;

import com.luoyu.blog.common.constant.AbstractConstant;
import com.luoyu.blog.common.exception.ExceptionFactory;
import com.luoyu.blog.common.response.ResponseMsg;
import com.luoyu.blog.framework.redis.redisutil.RedisUtil;
import com.luoyu.blog.project.service.shiroservice.ISyncCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Redis锁服务类
 */
@Slf4j
@Service
public class SyncCacheServiceImpl implements ISyncCacheService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ExceptionFactory exceptionFactory;

    /**
     * @author jinhaoxun
     * @description 获取redis中key的锁，乐观锁实现
     * @param lockName 获取锁的名字
     * @param expireTime 锁的失效时间
     * @return Boolean 是否获取成功
     * @throws Exception
     */
    @Override
    public boolean getLock(String lockName, int expireTime) throws Exception{
        boolean result = false;
        try {
            boolean isExist = redisUtil.exists(lockName);
            if(!isExist){
                redisUtil.incr(lockName,0);
                redisUtil.expire(lockName,expireTime<=0? AbstractConstant.REFRESH_TOKEN_CHECK_EXPIRATION_TIME:expireTime);
            }
            long reVal =  redisUtil.incr(lockName,1);
            if(1L ==reVal){
                //获取锁
                result = true;
                log.info("获取redis锁:"+lockName+",成功");
            }else {
                log.info("获取redis锁:"+lockName+",失败"+reVal);
            }
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.REDIS_GET_LOCK_FAIL.getCode(),"获取redis锁失败:"+lockName+e.getMessage());
        }
        return result;
    }

    /**
     * @author jinhaoxun
     * @description 释放锁，直接删除key(直接删除会导致任务重复执行，所以释放锁机制设为超时20s)
     * @param lockName 释放锁的名字
     * @return Boolean 是否释放成功
     * @throws Exception
     */
    @Override
    public boolean releaseLock(String lockName) throws Exception {
        try {
            redisUtil.expire(lockName, AbstractConstant.RELEASE_LOCK_TIME);
            log.info("释放redis锁:"+lockName+",成功");
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.REDIS_RELEASE_LOCK_FAIL.getCode(),"释放redis锁失败:"+lockName+e.getMessage());
        }
        return false;
    }

    /**
     * @author jinhaoxun
     * @description 释放锁，直接删除key(直接删除会导致任务重复执行，所以释放锁机制设为超时20s)
     * @param lockName 释放锁的名字
     * @param releaseLockTime (单位：秒)
     * @return Boolean 是否释放成功
     * @throws Exception
     */
    @Override
    public boolean releaseLock(String lockName, int releaseLockTime) throws Exception{
        try {
            redisUtil.expire(lockName, releaseLockTime);
            log.info("释放redis锁:"+lockName+",成功");
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.REDIS_RELEASE_LOCK_FAIL.getCode(),"释放redis锁失败:"+lockName+e.getMessage());
        }
        return false;
    }
}
