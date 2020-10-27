package com.luoyu.blogmanage.common.aop.annotation;

import java.lang.annotation.*;

/**
 * RefreshEsMqSender
 *
 * @author luoyu
 * @date 2019/03/16 22:52
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshEsMqSender {

    String id();

    String content();

    String operation();

}
