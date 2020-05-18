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
 * @description 用户表实体类
 */
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("USER_ID")
    private Long userId;

    /**
     * 邮箱(设置唯一值,一个邮箱绑定一个账号)
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 手机号(设置唯一值,一个手机号绑定一个账号)
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 封号状态(正常使用：false 被封禁：true)
     */
    @TableField("BAN")
    private Boolean ban;

    /**
     * 账号状态(正常使用：true 注销：false)
     */
    @TableField("STATUS")
    private Boolean status;

    /**
     * 用户名(设置唯一值)
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 用户昵称
     */
    @TableField("NAME")
    private String name;

    /**
     * 年龄
     */
    @TableField("AGE")
    private Integer age;

    /**
     * 性别(保密：1 男：2 女：3 其他：4)
     */
    @TableField("SEX")
    private Integer sex;

    /**
     * 文章量
     */
    @TableField("ARTICLE")
    private Integer article;

    /**
     * 文章评论量
     */
    @TableField("ARTICLE_COMMENT")
    private Integer articleComment;

    /**
     * 文章点赞量
     */
    @TableField("ARTICLE_PRAISE")
    private Integer articlePraise;

    /**
     * 地址
     */
    @TableField("REGION")
    private String region;

    /**
     * 职业
     */
    @TableField("OCCUPATION")
    private String occupation;

    /**
     * 简介
     */
    @TableField("DYNAMIC")
    private String dynamic;

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


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getBan() {
        return ban;
    }

    public void setBan(Boolean ban) {
        this.ban = ban;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    public Integer getArticleComment() {
        return articleComment;
    }

    public void setArticleComment(Integer articleComment) {
        this.articleComment = articleComment;
    }

    public Integer getArticlePraise() {
        return articlePraise;
    }

    public void setArticlePraise(Integer articlePraise) {
        this.articlePraise = articlePraise;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDynamic() {
        return dynamic;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
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
        return this.userId;
    }

    @Override
    public String toString() {
        return "User{" +
        "userId=" + userId +
        ", email=" + email +
        ", phone=" + phone +
        ", ban=" + ban +
        ", status=" + status +
        ", userName=" + userName +
        ", password=" + password +
        ", name=" + name +
        ", age=" + age +
        ", sex=" + sex +
        ", article=" + article +
        ", articleComment=" + articleComment +
        ", articlePraise=" + articlePraise +
        ", region=" + region +
        ", occupation=" + occupation +
        ", dynamic=" + dynamic +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", updaterId=" + updaterId +
        "}";
    }
}
