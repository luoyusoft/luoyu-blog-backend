package com.luoyu.blog.project.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.common.entity.sys.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bobbi
 * @since 2018-10-08
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 根据roleId查询所属menuId
     * @param roleId
     * @return
     */
    List<Integer> queryMenuIdList(Integer roleId);
}
