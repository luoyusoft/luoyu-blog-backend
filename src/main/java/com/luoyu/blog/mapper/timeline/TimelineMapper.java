package com.luoyu.blog.mapper.timeline;

import com.luoyu.blog.entity.timeline.Timeline;
import com.luoyu.blog.entity.timeline.TimelinePost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TimeLineMapper
 *
 * @author luoyu
 * @date 2019/02/24 20:53
 * @description
 */
public interface TimelineMapper {

    List<TimelinePost> listTimelinePost(@Param("year") Integer year, @Param("month") Integer month);

    List<Timeline> listTimeline();

}
