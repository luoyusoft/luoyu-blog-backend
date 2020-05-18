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
 * @description 消息表实体类
 */
public class Message extends Model<Message> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("MESSAGE_ID")
    private Long messageId;

    /**
     * 内容
     */
    @TableField("CONTENT")
    private String content;

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


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return this.messageId;
    }

    @Override
    public String toString() {
        return "Message{" +
        "messageId=" + messageId +
        ", content=" + content +
        ", userId=" + userId +
        ", createTime=" + createTime +
        "}";
    }
}
