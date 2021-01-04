package com.luoyu.blog.common.aop.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * ViewLog
 *
 * @author luoyu
 * @date 2019/02/15 14:51
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(-1)
public @interface LogView {

     // 0：article，1：video，2：聊天室
     int module();

}
