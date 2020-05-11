package com.jinhaoxun.blog.project.tt.pojo.apply;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 二级标签表实体类
 */
public class SecondaryLabel extends Model<SecondaryLabel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("SECONDARY_ID")
    private Long secondaryId;

    /**
     * 归属一级标签编码(个人成长：1 心情随笔：2 技术分享：3 资源分享：4 )
     */
    @TableField("PRIMARY_CODE")
    private Integer primaryCode;

    /**
     * 标签名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 标签编码(后台：1 前端：2 数据库：3 等)
     */
    @TableField("CODE")
    private Integer code;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;


    public Long getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(Long secondaryId) {
        this.secondaryId = secondaryId;
    }

    public Integer getPrimaryCode() {
        return primaryCode;
    }

    public void setPrimaryCode(Integer primaryCode) {
        this.primaryCode = primaryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.secondaryId;
    }

    @Override
    public String toString() {
        return "SecondaryLabel{" +
        "secondaryId=" + secondaryId +
        ", primaryCode=" + primaryCode +
        ", name=" + name +
        ", code=" + code +
        ", createTime=" + createTime +
        "}";
    }
}
