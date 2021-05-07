package com.jinhx.blog.service.timeline;

import com.jinhx.blog.entity.timeline.Timeline;

import java.util.List;

/**
 * TimelineService
 *
 * @author luoyu
 * @date 2019/02/24
 * @description 时间线
 */
public interface TimelineService {

    /**
     * 获取时间线列表
     * @return 时间线列表
     */
    List<Timeline> listTimelines();

}
