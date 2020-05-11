package com.jinhaoxun.blog.project.tt.pojo.apply.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 分页响应实体类
 */
@Setter
@Getter
@ToString
public class PageRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总条数
     */
    private Integer total;

}
