package com.luoyu.blog.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 统一API响应结果封装
 */
@Setter
@Getter
@ToString
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    /**
     * @author jinhaoxun
     * @description 构造器
     * @param code 响应状态码
     * @param msg 响应信息
     * @param data 响应数据
     */
    ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
