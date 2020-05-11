package com.jinhaoxun.blog.project.tt.pojo.apply.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 更新文章请求实体类
 */
@Setter
@Getter
@ToString
public class UpdateArticleReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    @NotNull(message = "文章id不能为空")
    private Long articleId;

    /**
     * 一级标签
     */
    @NotNull(message = "一级标签不能为空")
    private Integer primaryLabelCode;

    /**
     * 二级标签
     */
    @NotNull(message = "二级标签不能为空")
    private Integer secondaryLabelCode;

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 更新人ID
     */
    @NotNull(message = "更新人id不能为空")
    private Long updaterId;

    /**
     * 内容
     */
    @NotNull(message = "内容不能为空")
    private String content;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

}
