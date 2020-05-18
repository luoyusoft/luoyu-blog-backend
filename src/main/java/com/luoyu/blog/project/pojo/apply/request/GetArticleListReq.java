package com.luoyu.blog.project.pojo.apply.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 获取文章列表请求实体类
 */
@Setter
@Getter
@ToString
public class GetArticleListReq extends PageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 一级标签
     */
    private Integer primaryLabelCode;

    /**
     * 二级标签
     */
    private Integer secondaryLabelCode;

    /**
     * 关键字
     */
    private String keyWord;

}
