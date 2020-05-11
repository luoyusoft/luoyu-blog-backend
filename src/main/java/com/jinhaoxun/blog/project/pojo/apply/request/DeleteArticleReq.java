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
 * @description 删除文章请求实体类
 */
@Setter
@Getter
@ToString
public class DeleteArticleReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    @NotNull(message = "文章id不能为空")
    private Long articleId;

}
