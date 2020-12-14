package com.luoyu.blog.common.aop.annotation;

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
public @interface LogLike {

     // 0：article，1：video
     int type();

}
