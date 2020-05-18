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
 * @description 文章表实体类
 */
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("ARTICLE_ID")
    private Long articleId;

    /**
     * 作者ID
     */
    @TableField("AUTHOR_ID")
    private Long authorId;

    /**
     * 文章标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 获赞量
     */
    @TableField("PRAISE_AMOUNT")
    private Integer praiseAmount;

    /**
     * 评论量
     */
    @TableField("COMMENT_AMOUNT")
    private Integer commentAmount;

    /**
     * 文章状态(草稿：1 已发表：2 )
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 阅读量
     */
    @TableField("READ_AMOUNT")
    private Integer readAmount;

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


    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPraiseAmount() {
        return praiseAmount;
    }

    public void setPraiseAmount(Integer praiseAmount) {
        this.praiseAmount = praiseAmount;
    }

    public Integer getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(Integer commentAmount) {
        this.commentAmount = commentAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReadAmount() {
        return readAmount;
    }

    public void setReadAmount(Integer readAmount) {
        this.readAmount = readAmount;
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

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    @Override
    protected Serializable pkVal() {
        return this.articleId;
    }

    @Override
    public String toString() {
        return "Article{" +
        "articleId=" + articleId +
        ", authorId=" + authorId +
        ", title=" + title +
        ", content=" + content +
        ", praiseAmount=" + praiseAmount +
        ", commentAmount=" + commentAmount +
        ", status=" + status +
        ", readAmount=" + readAmount +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", updaterId=" + updaterId +
        "}";
    }
}
