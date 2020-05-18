package com.luoyu.blog.project.pojo.shiro;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 上下文实体类
 */
public class UserContext implements AutoCloseable {

    /**
     * 实现不同线程存储不同内容，不共享
     */
    static final ThreadLocal<LoginUser> CURRENT = new ThreadLocal<>();

    /**
     * @author jinhaoxun
     * @description 构造器
     * @param user 用户信息
     */
    public UserContext(LoginUser user) {
        CURRENT.set(user);
    }

    public static LoginUser getCurrentUser() {
        return CURRENT.get();
    }

    @Override
    public void close() {
        CURRENT.remove();
    }
}
