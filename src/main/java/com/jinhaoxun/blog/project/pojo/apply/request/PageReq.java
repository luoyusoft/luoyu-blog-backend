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
 * @description 分页请求实体类
 */
@Setter
@Getter
@ToString
public class PageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页数
     */
    @NotNull(message = "当前页数不能为空")
    private Integer currentPage;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;

}
