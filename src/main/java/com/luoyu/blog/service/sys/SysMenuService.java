package com.luoyu.blog.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.entity.sys.SysMenu;

import java.util.List;

/**
 * SysMenuService
 *
 * @author luoyu
 * @date 2018/10/22 12:31
 * @description
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取用户的所有菜单
     * @param userId
     * @return
     */
    List<SysMenu> listUserMenu(Integer userId);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<SysMenu> queryListParentId(Integer parentId, List<Integer> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<SysMenu> queryListParentId(Integer parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenu> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<SysMenu> getUserMenuList(Integer userId);

    /**
     * 删除
     */
    void delete(Integer menuId);

}
