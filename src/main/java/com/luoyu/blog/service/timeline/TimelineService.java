package com.luoyu.blog.service.timeline;

import com.luoyu.blog.entity.timeline.Timeline;

import java.util.List;

/**
 * TimeLineService
 *
 * @author luoyu
 * @date 2019/02/24
 * @description 时间线
 */
public interface TimelineService {

    /**
     * 获取timeLine数据
     * @return 时间线列表
     */
    List<Timeline> listTimeLine();

}
