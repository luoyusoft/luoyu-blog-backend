package com.luoyu.blog.controller.portal.timeline;

import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.timeline.Timeline;
import com.luoyu.blog.service.timeline.TimelineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TimeLineController
 *
 * @author luoyu
 * @date 2019/02/24 20:46
 * @description
 */
@RestController
@RequestMapping("/timeline")
public class TimelineController {

    @Resource
    private TimelineService timelineService;

    @GetMapping("")
    public Response listTimeline() {
        List<Timeline> timelineList = timelineService.listTimeLine();

        return Response.success(timelineList);
    }

}
