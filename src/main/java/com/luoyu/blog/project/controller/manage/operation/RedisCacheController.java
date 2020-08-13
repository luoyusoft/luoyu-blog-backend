package com.luoyu.blog.project.controller.manage.operation;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * BookNoteAdminController
 *
 * @author bobbi
 * @date 2019/11/12 20:25
 * @email 571002217@qq.com
 * @description
 */
@RestController
@RequestMapping("/admin/operation/redis")
public class RedisCacheController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


}
