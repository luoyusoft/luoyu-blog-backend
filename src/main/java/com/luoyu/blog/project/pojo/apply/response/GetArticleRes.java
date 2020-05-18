package com.luoyu.blog.project.pojo.apply.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 获取文章响应实体类
 */
@Setter
@Getter
@ToString
public class GetArticleRes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long articleId;

    private String title;

    private String content;

    private Integer praiseAmount;

    private Integer commentAmount;

    /**
     * 文章状态(草稿：1 已发表：2 )
     */
    private Integer status;

    private Integer readAmount;

    private Date createTime;

    private Date updateTime;

    private int primaryLabel;

    private int secondaryLabel;

}
