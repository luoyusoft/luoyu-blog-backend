package com.luoyu.blogmanage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.sys.SysRole;
import com.luoyu.blogmanage.common.util.PageUtils;

import java.util.List;
import java.util.Map;

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
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

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
