package com.luoyu.blog.controller.cache;

import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.service.cache.CacheServer;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CacheController
 *
 * @author luoyu
 * @date 2019/11/12 20:25
 * @description
 */
@RestController
public class CacheController {

    @Autowired
    private CacheServer cacheServer;

    /**
     * 清除所有缓存
     */
    @DeleteMapping("/manage/cache/cleanAll")
    @RequiresPermissions("cache:cleanAll")
    public Response cleanAllCache() {
        cacheServer.cleanAllCache();
        return Response.success();
    }

}
