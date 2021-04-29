package com.jinhx.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinhx.blog.entity.sys.SysRole;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2018-10-08
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询所属角色
     * @param createUserId
     * @return
     */
    List<Integer> queryRoleIdList(Integer createUserId);

}
