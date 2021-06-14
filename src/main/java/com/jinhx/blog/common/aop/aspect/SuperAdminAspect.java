package com.jinhx.blog.common.aop.aspect;

import com.jinhx.blog.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SuperAdminAspect
 * @author jinhx
 * @date 2019/02/15 14:56
 * @description 超级管理员权限
 */
@Aspect
@Component
@Slf4j
public class SuperAdminAspect {

    @Pointcut("@annotation(com.jinhx.blog.common.aop.annotation.SuperAdmin)")
    public void superAdminPointCut() {

    }

    @Before("superAdminPointCut()")
    @Transactional(rollbackFor = Exception.class)
    public void before(JoinPoint joinPoint) throws Throwable {
        // 如果不是超级管理员，则无权限
        SysAdminUtils.checkSuperAdmin();
    }

}
