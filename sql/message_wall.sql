create table message_wall
(
    id          int auto_increment comment '主键'
        primary key,
    floor_num   int                                not null comment '楼层数',
    reply_id    int                               not null comment '回复id，-1为层主',
    name        varchar(64)                       not null comment '昵称',
    email       varchar(64)                        null comment '邮箱',
    comment     varchar(2048)                     not null comment '内容',
    profile     varchar(1024)                      null comment '头像地址',
    website     varchar(1024)                      null comment '网站',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '留言墙表';