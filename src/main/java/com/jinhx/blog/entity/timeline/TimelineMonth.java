package com.jinhx.blog.entity.timeline;

import lombok.Data;

import java.util.List;

/**
 * TimelineMonh
 *
 * @author luoyu
 * @date 2019/02/24 20:33
 * @description
 */
@Data
public class TimelineMonth {

    private Integer month;

    private Integer count;

    private List<TimelinePost> posts;

}
