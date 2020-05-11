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
 * @description 文章评论表实体类
 */
public class ArticleComment extends Model<ArticleComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("COMMENT_ID")
    private Long commentId;

    /**
     * 楼层
     */
    @TableField("FLOOR")
    private Integer floor;

    /**
     * 内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 评论文章ID
     */
    @TableField("ARTICLE_ID")
    private Long articleId;

    /**
     * 评论用户ID
     */
    @TableField("FROM_ID")
    private Long fromId;

    /**
     * 评论目标用户ID
     */
    @TableField("TO_ID")
    private Long toId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;


    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.commentId;
    }

    @Override
    public String toString() {
        return "ArticleComment{" +
        "commentId=" + commentId +
        ", floor=" + floor +
        ", content=" + content +
        ", articleId=" + articleId +
        ", fromId=" + fromId +
        ", toId=" + toId +
        ", createTime=" + createTime +
        "}";
    }
}
