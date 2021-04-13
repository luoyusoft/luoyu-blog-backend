package com.luoyu.blog.service.timeline.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.luoyu.blog.entity.timeline.Timeline;
import com.luoyu.blog.entity.timeline.TimelineMonth;
import com.luoyu.blog.entity.timeline.TimelinePost;
import com.luoyu.blog.mapper.timeline.TimelineMapper;
import com.luoyu.blog.service.timeline.TimelineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * TimeLineServiceImpl
 *
 * @author luoyu
 * @date 2019/02/24 20:47
 * @description 时间线
 */
@Service
public class TimelineServiceImpl implements TimelineService {

    @Resource
    private TimelineMapper timelineMapper;

    /**
     * 获取timeLine数据
     *
     * @return 时间线列表
     */
    @Override
    public List<Timeline> listTimeLine() {
        List<Timeline> timelineList = timelineMapper.listTimeline();
        genTimelineMonth(timelineList);
        return timelineList;
    }

    private void genTimelineMonth(List<Timeline> timelineList) {
        for(Timeline timeline : timelineList) {
            List<TimelineMonth> timelineMonthList = new ArrayList<>();
           for (int i = Calendar.DECEMBER + 1; i > 0; i--) {
               List<TimelinePost> postList = timelineMapper.listTimelinePost(timeline.getYear(), i);
               if(CollectionUtils.isEmpty(postList)) {
                   continue;
               }
               TimelineMonth month = new TimelineMonth();
               month.setCount(postList.size());
               month.setMonth(i);
               month.setPosts(postList);
               timelineMonthList.add(month);
           }
           timeline.setMonths(timelineMonthList);
        }
    }

}
