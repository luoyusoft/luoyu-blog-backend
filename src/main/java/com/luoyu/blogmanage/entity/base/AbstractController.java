package com.luoyu.blogmanage.entity.base;

import com.luoyu.blogmanage.entity.sys.SysUser;
import org.apache.shiro.SecurityUtils;

/**
 * AbstractController
 *
 * @author luoyu
 * @date 2018/10/22 12:35
 * @description
 */
public class AbstractController {

    protected SysUser getUser(){
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected Integer getUserId(){
        return getUser().getUserId();
    }

}
