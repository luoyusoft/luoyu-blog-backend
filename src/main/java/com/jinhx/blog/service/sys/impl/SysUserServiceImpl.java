package com.jinhx.blog.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.common.util.SysAdminUtils;
import com.jinhx.blog.entity.sys.SysUser;
import com.jinhx.blog.entity.sys.dto.SysUserDTO;
import com.jinhx.blog.mapper.sys.SysUserMapper;
import com.jinhx.blog.service.sys.SysUserRoleService;
import com.jinhx.blog.service.sys.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * SysUserServiceImpl
 * @author jinhx
 * @since 2018-10-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;


    /**
     * 查询用户菜单列表
     * @param userId 用户id
     * @return 用户菜单列表
     */
    @Override
    public List<Integer> queryAllMenuId(Integer userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    /**
     * 分页查询用户信息列表
     * @param page 页码
     * @param limit 页数
     * @param username 用户名
     * @return 用户信息列表
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("username", username);

        IPage<SysUser> sysUserPage = baseMapper.selectPage(
                new Query<SysUser>(params).getPage(),
                new QueryWrapper<SysUser>().lambda()
                        .like(StringUtils.isNotBlank(username),SysUser::getUsername, username));

        List<SysUserDTO> sysUserDTOList = new ArrayList<>();
        sysUserPage.getRecords().forEach(item -> {
            SysUserDTO sysUserDTO = new SysUserDTO();
            BeanUtils.copyProperties(item, sysUserDTO);
            sysUserDTO.setRoleNameStr(String.join(",", sysUserRoleService.queryRoleNameList(item.getId())));
            sysUserDTOList.add(sysUserDTO);
        });

        IPage<SysUserDTO> sysUserDTOPage = new Page<>();
        BeanUtils.copyProperties(sysUserPage, sysUserDTOPage);
        sysUserDTOPage.setRecords(sysUserDTOList);

        return new PageUtils(sysUserDTOPage);
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    @Override
    public boolean updatePassword(Integer userId, String password, String newPassword) {
        SysUser sysUser=new SysUser();
        sysUser.setPassword(newPassword);
        return this.update(sysUser, new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getId,userId).eq(SysUser::getPassword,password));
    }

    /**
     * 新增用户信息
     * @param sysUserDTO 用户信息
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSysUser(SysUserDTO sysUserDTO) {
        if (baseMapper.countSysUserByUsername(sysUserDTO.getUsername()) > 0){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该用户名已存在");
        }

        sysUserDTO.setCreateTime(LocalDateTime.now());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUserDTO.setPassword(new Sha256Hash(sysUserDTO.getPassword(), salt).toHex());
        sysUserDTO.setSalt(salt);
        baseMapper.insert(sysUserDTO);

        // 如果新增超级管理员，需要当前用户拥有超级管理员权限
        if (!CollectionUtils.isEmpty(sysUserDTO.getRoleIdList()) && SysAdminUtils.isHaveSuperAdmin(sysUserDTO.getRoleIdList())){
            SysAdminUtils.checkSuperAdmin();
        }

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(sysUserDTO.getId(), sysUserDTO.getRoleIdList());
        return true;
    }

    /**
     * 根据用户id更新用户信息
     * @param sysUserDTO 用户信息
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysUserById(SysUserDTO sysUserDTO) {
        SysUser sysUser = baseMapper.selectById(sysUserDTO.getId());
        if (!sysUser.getUsername().equals(sysUserDTO.getUsername()) && baseMapper.countSysUserByUsername(sysUserDTO.getUsername()) > 0){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该用户名已存在");
        }

        if(StringUtils.isBlank(sysUserDTO.getPassword())){
            sysUserDTO.setPassword(null);
        }else{
            sysUserDTO.setPassword(new Sha256Hash(sysUserDTO.getPassword(), sysUserDTO.getSalt()).toHex());
        }
        baseMapper.updateById(sysUserDTO);

        // 如果修改包含超级管理员，需要当前用户拥有超级管理员权限
        if (!CollectionUtils.isEmpty(sysUserDTO.getRoleIdList()) &&
                (SysAdminUtils.isHaveSuperAdmin(sysUserDTO.getRoleIdList()) || SysAdminUtils.isHaveSuperAdmin(sysUserRoleService.getRoleIdListByUserId(sysUserDTO.getId())))){
            SysAdminUtils.checkSuperAdmin();
        }

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(sysUserDTO.getId(), sysUserDTO.getRoleIdList());
        return true;
    }

    /**
     * 根据用户id列表批量删除用户
     * @param userIds 用户id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] userIds) {
        // 如果包括超级管理员，需要当前用户拥有超级管理员权限
        if (sysUserRoleService.isHaveSuperAdmin(userIds)){
            SysAdminUtils.checkSuperAdmin();
        }

        this.removeByIds(Arrays.asList(userIds));
        //删除用户与角色关联
        sysUserRoleService.deleteBatchByUserId(userIds);

        return true;
    }

    /**
     * 根据用户id查询用户有权限所有菜单列表
     * @param userId 用户id
     * @return 用户有权限所有菜单列表
     */
    @Override
    public List<String> getAllPermsByUserId(Integer userId) {
        return baseMapper.getAllPermsByUserId(userId);
    }

    /**
     * 根据用户名获取SysUserDTO
     * @param username 用户名
     * @return SysUserDTO
     */
    @Override
    public SysUserDTO getSysUserDTOByUsername(String username) {
        SysUser sysUser = baseMapper.getSysUserByUsername(username);
        if (sysUser == null){
            return null;
        }
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(sysUser, sysUserDTO);
        sysUserDTO.setRoleIdList(sysUserRoleService.getRoleIdListByUserId(sysUser.getId()));
        return sysUserDTO;
    }

    /**
     * 根据用户id获取SysUserDTO
     * @param userId 用户id
     * @return SysUserDTO
     */
    @Override
    public SysUserDTO getSysUserDTOByUserId(Integer userId) {
        SysUser sysUser = baseMapper.getSysUserByUserId(userId);
        if (sysUser == null){
            return null;
        }
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(sysUser, sysUserDTO);
        sysUserDTO.setRoleIdList(sysUserRoleService.getRoleIdListByUserId(sysUser.getId()));
        return sysUserDTO;
    }

    /**
     * 根据用户id获取用户昵称
     * @param userId 用户id
     * @return 用户昵称
     */
    @Override
    public String getNicknameByUserId(Integer userId) {
        return baseMapper.getNicknameByUserId(userId);
    }

}
