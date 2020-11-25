package com.luoyu.blogmanage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.entity.sys.SysRole;

import java.util.List;

/**
 * SysRoleService
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色
     * @param page
     * @param limit
     * @param roleName
     * @param createUserId
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String roleName, Integer createUserId);

    /**
     * 批量删除
     * @param roleIds
     */
    void deleteBatch(Integer[] roleIds);

    /**
     * 查询roleId
     * @param createUserId
     * @return
     */
    List<Integer> queryRoleIdList(Integer createUserId);

}
