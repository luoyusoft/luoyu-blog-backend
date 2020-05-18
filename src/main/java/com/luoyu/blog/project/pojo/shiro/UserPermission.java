package com.luoyu.blog.project.pojo.shiro;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户权限表实体类
 */
public class UserPermission extends Model<UserPermission> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("USER_PERMISSION_ID")
    private Long userPermissionId;

    /**
     * 关联权限类型(管理员操作：A 普通用户操作：B VIP用户操作：C )
     */
    @TableField("TYPE")
    private String type;

    /**
     * 关联权限编码(管理员：1 普通用户：2 VIP用户：3 )
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


    public Long getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Long userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return this.userPermissionId;
    }

    @Override
    public String toString() {
        return "UserPermission{" +
        "userPermissionId=" + userPermissionId +
        ", type=" + type +
        ", code=" + code +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", createrId=" + createrId +
        "}";
    }
}
