package com.luoyu.blog.controller.portal.operation;

import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.dto.TagDTO;
import com.luoyu.blog.service.operation.TagService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TagController
 *
 * @author luoyu
 * @date 2019/02/22 16:34
 * @description
 */
@RestController("tagPortalController")
@CacheConfig(cacheNames = RedisCacheNames.TAG)
@RequestMapping("/operation")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/tags")
    @Cacheable
    public Response listTag() {
        List<TagDTO> tagList = tagService.listTagDTO();
        return Response.success(tagList);
    }

}
