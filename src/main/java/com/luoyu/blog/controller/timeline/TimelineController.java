package com.luoyu.blog.controller.timeline;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.timeline.Timeline;
import com.luoyu.blog.service.timeline.TimelineService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TimeLineController
 *
 * @author luoyu
 * @date 2019/02/24
 * @description 时间线
 */
@RestController
@CacheConfig(cacheNames ={RedisKeyConstants.TIMELINES})
public class TimelineController {

    @Resource
    private TimelineService timelineService;

    /********************** portal ********************************/

    @GetMapping("/timeline")
    @Cacheable
    @LogView(module = 4)
    public Response listTimeline() {
        List<Timeline> timelineList = timelineService.listTimeLine();
        return Response.success(timelineList);
    }

}
