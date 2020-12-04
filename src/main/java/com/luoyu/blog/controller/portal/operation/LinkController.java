package com.luoyu.blog.controller.portal.operation;

import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Link;
import com.luoyu.blog.service.operation.LinkService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * LinkController
 *
 * @author luoyu
 * @date 2019/02/21 17:09
 * @description
 */
@RequestMapping("/operation")
@CacheConfig(cacheNames = RedisCacheNames.LINK)
@RestController("LinkPortalController")
public class LinkController {

    @Resource
    private LinkService linkService;

    @RequestMapping("/links")
    @Cacheable
    public Response listLink() {
        List<Link> linkList = linkService.listLink();
        return Response.success(linkList);
    }

}
