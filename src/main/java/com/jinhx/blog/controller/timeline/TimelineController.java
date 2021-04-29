package com.jinhx.blog.controller.timeline;

import com.jinhx.blog.common.aop.annotation.LogView;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.timeline.Timeline;
import com.jinhx.blog.service.timeline.TimelineService;
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
public class TimelineController {

    @Resource
    private TimelineService timelineService;

    /********************** portal ********************************/

    @GetMapping("/timeline")
    @LogView(module = 4)
    public Response listTimeline() {
        List<Timeline> timelineList = timelineService.listTimeLine();
        return Response.success(timelineList);
    }

}
