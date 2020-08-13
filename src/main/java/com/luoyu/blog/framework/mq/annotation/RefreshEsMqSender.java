package com.luoyu.blog.framework.mq.annotation;

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

    String sender();

    String msg() default "send refresh msg to ElasticSearch";

}
