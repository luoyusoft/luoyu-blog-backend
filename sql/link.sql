create table friend_link
(
    id          int auto_increment comment '主键'
        primary key,
    title       varchar(64)                        not null comment '链接名称',
    url         varchar(1024)                      not null comment '链接地址',
    avatar      varchar(1024)                      null comment '链接头像',
    description varchar(256)                       null comment '链接简介',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '友链';
