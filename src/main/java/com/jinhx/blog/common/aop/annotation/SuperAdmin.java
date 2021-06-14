package com.jinhx.blog.common.aop.annotation;

import java.lang.annotation.*;

/**
 * SuperAdmin
 * @author jinhx
 * @date 2019/02/15 14:51
 * @description 超级管理员权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SuperAdmin {

}
