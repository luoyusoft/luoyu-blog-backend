package com.luoyu.blog.controller.portal.operation;

import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.dto.RecommendDTO;
import com.luoyu.blog.service.operation.RecommendService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Recommend
 *
 * @author luoyu
 * @date 2019/02/22 13:40
 * @description
 */
@RestController("recommendPortalController")
@CacheConfig(cacheNames = RedisCacheNames.RECOMMEND)
@RequestMapping("/operation")
public class RecommendController {

    @Resource
    private RecommendService recommendService;

    @RequestMapping("/recommends")
    @Cacheable(key = "'RECOMMEND'")
    public Response listRecommend() {
        List<RecommendDTO> recommendList = recommendService.listRecommendDTO();
        return Response.success(recommendList);
    }

}
