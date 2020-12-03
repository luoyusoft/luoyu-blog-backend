package com.luoyu.blogmanage.controller.operation;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * RedisCacheController
 *
 * @author luoyu
 * @date 2019/11/12 20:25
 * @description
 */
@RestController
@RequestMapping("/admin/operation/redis")
public class RedisCacheController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

}
