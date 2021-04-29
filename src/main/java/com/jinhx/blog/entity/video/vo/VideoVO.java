package com.jinhx.blog.entity.video.vo;

import com.jinhx.blog.entity.operation.Tag;
import com.jinhx.blog.entity.video.Video;
import lombok.Data;

import java.util.List;

/**
 * ArticleVO
 *
 * @author luoyu
 * @date 2019/01/08 19:04
 * @description
 */
@Data
public class VideoVO extends Video {

    /**
     * 所属分类，以逗号分隔
     */
    private String categoryListStr;

    /**
     * 所属标签
     */
    private List<Tag> tagList;

    /**
     * 推荐
     */
    private Boolean recommend;

    /**
     * 置顶
     */
    private Boolean top;

}
