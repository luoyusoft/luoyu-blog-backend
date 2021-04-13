create table article
(
    id             int auto_increment comment '主键'
        primary key,
    title          varchar(128)                         null comment '文章标题',
    description    varchar(1024)                        null comment '文章描述',
    author         varchar(64)                          null comment '文章作者',
    content        longtext                             null comment '文章内容',
    content_format longtext                             null comment 'html的content',
    read_num       int        default 0                 not null comment '阅读量',
    comment_num    int        default 0                 not null comment '评论量',
    like_num       int        default 0                 not null comment '点赞量',
    cover_type     int                                  null comment '文章展示类别（1:普通，2：大图片，3：无图片）',
    cover          varchar(1024)                        null comment '封面',
    category_id    varchar(64)                          null comment '文章分类类别（存在多级分类，用逗号隔开）',
    publish        tinyint(1) default 0                 not null comment '发布状态',
    create_time    datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime   default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '文章';
