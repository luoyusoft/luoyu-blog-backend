package com.luoyu.blog.project.controller.portal.common.annotation;

import java.lang.annotation.*;

/**
 * ViewLog
 *
 * @author bobbi
 * @date 2019/02/15 14:51
 * @email 571002217@qq.com
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogView {

     String type();
}
