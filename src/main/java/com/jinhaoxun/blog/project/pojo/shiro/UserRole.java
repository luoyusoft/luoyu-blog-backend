package com.jinhaoxun.blog.project.tt.pojo.shiro;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户角色表实体类
 */
public class UserRole extends Model<UserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("USER_ROLE_ID")
    private Long userRoleId;

    /**
     * 关联角色编码(管理员：1 普通用户：2 VIP用户：3 )
     */
    @TableField("CODE")
    private Integer code;

    /**
     * 关联用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField("CREATER_ID")
    private Long createrId;


    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Long createrId) {
        this.createrId = createrId;
    }

    @Override
    protected Serializable pkVal() {
        return this.userRoleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
        "userRoleId=" + userRoleId +
        ", code=" + code +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", createrId=" + createrId +
        "}";
    }
}
