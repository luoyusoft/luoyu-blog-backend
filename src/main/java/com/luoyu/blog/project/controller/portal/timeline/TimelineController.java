package com.luoyu.blog.project.controller.portal.timeline;

import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.entity.timeline.Timeline;
import com.luoyu.blog.project.service.portal.timeline.TimelineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TimeLineController
 *
 * @author bobbi
 * @date 2019/02/24 20:46
 * @email 571002217@qq.com
 * @description
 */
@RestController
@RequestMapping("/timeline")
public class TimelineController {

    @Resource
    private TimelineService timelineService;

    @GetMapping("")
    public Result listTimeline() {
        List<Timeline> timelineList = timelineService.listTimeLine();

        return Result.ok().put("timelineList",timelineList);
    }
}
