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
 * @description 文章点赞表实体类
 */
public class ArticlePraise extends Model<ArticlePraise> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("PRAISE_ID")
    private Long praiseId;

    /**
     * 点赞文章ID
     */
    @TableField("ARTICLE_ID")
    private Long articleId;

    /**
     * 点赞用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;


    public Long getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(Long praiseId) {
        this.praiseId = praiseId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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

    @Override
    protected Serializable pkVal() {
        return this.praiseId;
    }

    @Override
    public String toString() {
        return "ArticlePraise{" +
        "praiseId=" + praiseId +
        ", articleId=" + articleId +
        ", userId=" + userId +
        ", createTime=" + createTime +
        "}";
    }
}
