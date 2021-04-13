create table sys_user
(
    id             int auto_increment comment '主键'
        primary key,
    username       varchar(128)                         not null comment '用户名',
    password       varchar(256)                         not null comment '密码',
    email          varchar(128)                         not null comment '邮箱',
    salt           varchar(32)                          null comment '密码盐',
    create_user_id int                                  not null comment '创建者id',
    status         tinyint(1) default 1                 not null comment '用户状态（0：禁用，1：正常）',
    create_time    datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime   default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '用户';
