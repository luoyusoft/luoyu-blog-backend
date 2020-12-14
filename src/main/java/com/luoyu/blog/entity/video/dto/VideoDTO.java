package com.luoyu.blog.entity.video.dto;

import com.luoyu.blog.entity.operation.Tag;
import com.luoyu.blog.entity.video.Video;
import lombok.Data;

import java.util.List;

/**
 * ArticleDTO
 *
 * @author luoyu
 * @date 2019/01/09 16:51
 * @description 文章列表VO
 */
@Data
public class VideoDTO extends Video {

    /**
     * 所属分类，以逗号分隔
     */
    private String categoryListStr;

    /**
     * 所属标签
     */
    private List<Tag> tagList;

}
