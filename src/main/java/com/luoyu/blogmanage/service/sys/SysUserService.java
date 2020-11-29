package com.luoyu.blogmanage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.sys.SysUser;
import com.luoyu.blogmanage.common.util.PageUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-10-08
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 查询用户菜单
     * @param userId
     * @return
     */
    List<Integer> queryAllMenuId(Integer userId);

    /**
     * 分页查询用户信息
     * @param page
     * @param limit
     * @param username
     * @param createUserId
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String username, Integer createUserId);

    /**
     * 更新密码
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    boolean updatePassword(Integer userId, String password, String newPassword);

    /**
     * 批量删除用户
     * @param userIds
     */
    void deleteBatch(Integer[] userIds);

}
