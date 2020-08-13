package com.luoyu.blog.project.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.common.entity.sys.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * SysUserRoleMapper
 *
 * @author Zoey
 * @date 2018/10/26 00:02
 * @email 571002217@qq.com
 * @description
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 查询roleId
     * @param userId
     * @return
     */
    List<Integer> queryRoleIdList(Integer userId);
}
