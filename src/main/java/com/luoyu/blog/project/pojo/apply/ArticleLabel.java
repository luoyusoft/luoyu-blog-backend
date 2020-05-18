package com.luoyu.blog.project.pojo.apply;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章标签表实体类
 */
public class ArticleLabel extends Model<ArticleLabel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("LABEL_ID")
    private Long labelId;

    /**
     * 关联文章ID
     */
    @TableField("ARTICLE_ID")
    private Long articleId;

    /**
     * 关联一级标签编码(个人成长：1 心情随笔：2 技术分享：3 资源分享：4 )
     */
    @TableField("PRIMARY_CODE")
    private Integer primaryCode;

    /**
     * 关联二级标签编码(后台：1 前端：2 数据库：3 等)
     */
    @TableField("SECONDARY_CODE")
    private Integer secondaryCode;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATER_ID")
    private Long updaterId;

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getPrimaryCode() {
        return primaryCode;
    }

    public void setPrimaryCode(Integer primaryCode) {
        this.primaryCode = primaryCode;
    }

    public Integer getSecondaryCode() {
        return secondaryCode;
    }

    public void setSecondaryCode(Integer secondaryCode) {
        this.secondaryCode = secondaryCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.labelId;
    }

    @Override
    public String toString() {
        return "ArticleLabel{" +
        "labelId=" + labelId +
        ", articleId=" + articleId +
        ", primaryCode=" + primaryCode +
        ", secondaryCode=" + secondaryCode +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", updaterId=" + updaterId +
        "}";
    }
}
