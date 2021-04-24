create table sys_param
(
    id          int auto_increment comment '主键'
        primary key,
    par_key     int                                not null comment '参数键',
    par_value   varchar(256)                       not null comment '参数值',
    menu_url    varchar(256)                       null comment '菜单url',
    type        varchar(256)                       not null comment '参数类型',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '系统参数';
