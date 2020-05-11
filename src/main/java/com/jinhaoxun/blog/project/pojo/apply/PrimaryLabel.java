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
 * @description 一级标签表实体类
 */
public class PrimaryLabel extends Model<PrimaryLabel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("PRIMARY_ID")
    private Long primaryId;

    /**
     * 标签名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 标签编码(个人成长：1 心情随笔：2 技术分享：3 资源分享：4 )
     */
    @TableField("CODE")
    private Integer code;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;


    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
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
        return this.primaryId;
    }

    @Override
    public String toString() {
        return "PrimaryLabel{" +
        "primaryId=" + primaryId +
        ", name=" + name +
        ", code=" + code +
        ", createTime=" + createTime +
        "}";
    }
}
