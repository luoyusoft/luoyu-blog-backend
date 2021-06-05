[![](https://img.shields.io/badge/个人博客-在线地址-green.svg)](https://jinhx.cc)
[![](https://img.shields.io/badge/后台管理系统-在线地址-blue.svg)](https://manage.jinhx.cc)

# Blog

> 基于SpringBoot 2.x + Vue 2.x搭建的前后端分离的个人博客，包括后台管理系统。

## 简介

```
一个简洁美观的个人博客，系统具有简洁、规范的代码设计。
能很好的帮助你熟悉前后端分离架构及相关技术栈。
```

## 主要模块
manage.
```
主要包括：首页，文章，视频，时光轴，留言墙，关于，聊天室（暂不适配移动端），友链...
```

## 主要技术栈

|        Name         |    Version    |
| :-----------------: | :-----------: |
|     SpringBoot      | 2.3.10.RELEASE|
| SpringCloud Alibaba | 2.2.5.RELEASE |
|    MyBatis-Plius    |     3.4.2     |
|    Apache Shiro     |     1.7.1     |
|      WebSocket      | 2.3.4.RELEASE |
|         JDK         |      1.8      |
|        MySQL        |      5.7      |
|    ElasticSearch    |     7.7.0     |
|       Xxl-Job       |     2.2.0     |
|        Minio        |     8.1.0     |
|        Nacos        |     1.3.2     |
|      RabbitMQ       |     3.8.5     |
|        Redis        |      5.0      |
|       Docker        |   19.03.13    |

## 部署
##### 为了防止被有心人恶意攻击，需要Nacos配置文件内容的，请另外联系我获取！
```
docker run -dit --name blog -v /usr/local/docker/blog/log:/usr/local/project/blog/log -v /etc/localtime:/etc/localtime:ro -p 8800:8800 -p 9999:9999 -p 465:465 jinhx128/blog:latest
```

## 在线地址

会持续更新，欢迎大家Star，感谢！

> 个人博客-->【<b><a href="https://jinhx.cc"> https://jinhx.cc </a></b>】

> 后台管理系统-->【<b><a href="https://manage.jinhx.cc"> https://manage.jinhx.cc </a></b>】
>
> 后台管理系统-->游客账号：guest，密码：guest

## 前端项目地址

会持续更新，欢迎大家Star，感谢！

> 个人博客-->【<b><a href="https://github.com/Jinhx128/blog-frontend"> https://github.com/Jinhx128/blog-frontend </a></b>】

> 后台管理系统-->【<b><a href="https://github.com/Jinhx128/blog-manage-frontend"> https://github.com/Jinhx128/blog-manage-frontend </a></b>】

## 参考教程

|序号|文章标题|
|:---:|:---|
|01|[IDEA2020年最新全家桶通用激活码分享（持续更新）](https://jinhx.cc/article/37)|

## 关于作者

【<b>个人博客</b>】    【<b><a href="https://jinhx.cc"> https://jinhx.cc </a></b>】