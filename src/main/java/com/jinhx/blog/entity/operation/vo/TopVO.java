package com.jinhx.blog.entity.operation.vo;

import com.jinhx.blog.entity.operation.Tag;
import com.jinhx.blog.entity.operation.Top;
import lombok.Data;

import java.util.List;

/**
 * TopDTO
 *
 * @author luoyu
 * @date 2019/02/22 10:49
 * @description
 */
@Data
public class TopVO extends Top {

    private String description;

    private Long readNum;

    private Long watchNum;

    private Long likeNum;

    private String cover;

    private List<Tag> tagList;

}
