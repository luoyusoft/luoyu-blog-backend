package com.jinhx.blog.common.aop.annotation;

import java.lang.annotation.*;

/**
 * RefreshEsMqSender
 * @author jinhx
 * @date 2019/03/16 22:52
 * @description RefreshEsMqSender
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshEsMqSender {

    String id();

    String content();

    String operation();

}
