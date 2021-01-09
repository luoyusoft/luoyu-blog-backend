package com.luoyu.blog.entity.operation.vo;

import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * RecommendDTO
 *
 * @author luoyu
 * @date 2019/02/22 10:49
 * @description
 */
@Data
public class RecommendVO extends Recommend {

    private String description;

    private Long readNum;

    private Long watchNum;

    private Long commentNum;

    private Long likeNum;

    private String cover;

    private List<Tag> tagList;

}
