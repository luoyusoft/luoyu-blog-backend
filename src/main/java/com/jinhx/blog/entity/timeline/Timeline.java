package com.jinhx.blog.entity.timeline;

import lombok.Data;

import java.util.List;

/**
 * Timeline
 *
 * @author luoyu
 * @date 2019/02/24 20:33
 * @description
 */
@Data
public class Timeline {

    private Integer year;

    private Integer count;

    private Boolean open;

    private List<TimelineMonth> months;

}
