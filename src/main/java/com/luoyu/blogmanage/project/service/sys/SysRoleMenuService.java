package com.luoyu.blogmanage.project.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.common.entity.sys.SysRoleMenu;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bobbi
 * @since 2018-10-08
 */
@Service
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void saveOrUpdate(Integer roleId, List<Integer> menuIdList);

    List<Integer> queryMenuIdList(Integer roleId);

    void deleteBatchByRoleId(Integer[] roleIds);
}
