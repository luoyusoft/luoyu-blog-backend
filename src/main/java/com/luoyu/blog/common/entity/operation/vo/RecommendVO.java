package com.luoyu.blog.common.entity.operation.vo;

import com.luoyu.blog.common.entity.operation.Recommend;
import com.luoyu.blog.common.entity.operation.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * RecommendVo
 *
 * @author bobbi
 * @date 2019/02/22 10:49
 * @email 571002217@qq.com
 * @description
 */
@Data
public class RecommendVO extends Recommend {

    private String description;

    private Long readNum;

    private Long commentNum;

    private Long likeNum;

    private String urlType;

    private String cover;

    private Date createTime;

    private List<Tag> tagList;

}
