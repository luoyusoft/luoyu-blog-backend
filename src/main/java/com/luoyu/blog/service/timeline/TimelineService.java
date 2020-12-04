package com.luoyu.blog.service.timeline;

import com.luoyu.blog.entity.timeline.Timeline;

import java.util.List;

/**
 * TimeLineService
 *
 * @author luoyu
 * @date 2019/02/24 20:47
 * @description
 */
public interface TimelineService {

    /**
     * 获取timeLine数据
     * @return
     */
    List<Timeline> listTimeLine();

}
