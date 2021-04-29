package com.jinhx.blog.common.aop.annotation;

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
public @interface LogView {

     // 0：article，1：video，2：聊天室
     int module();

}
