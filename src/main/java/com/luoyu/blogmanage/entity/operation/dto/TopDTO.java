package com.luoyu.blogmanage.entity.operation.dto;

import com.luoyu.blogmanage.entity.operation.Tag;
import com.luoyu.blogmanage.entity.operation.Top;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TopDTO
 *
 * @author luoyu
 * @date 2019/02/22 10:49
 * @description
 */
@Data
public class TopDTO extends Top {

    private String description;

    private Long readNum;

    private Long commentNum;

    private Long likeNum;

    private String urlType;

    private String cover;

    private LocalDateTime createTime;

    private List<Tag> tagList;

}
