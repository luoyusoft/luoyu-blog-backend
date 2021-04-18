package com.luoyu.blog.entity.article.vo;

import lombok.Data;

/**
 * HomeArticleInfoVO
 *
 * @author luoyu
 * @date 2019/01/08 19:04
 * @description
 */
@Data
public class HomeArticleInfoVO {

    /**
     * 总数量
     */
    private Integer allCount;

    /**
     * 已发布数量
     */
    private Integer publishCount;

}
