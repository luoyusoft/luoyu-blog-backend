create table sys_role
(
    id             int auto_increment comment '主键'
        primary key,
    role_name      varchar(64)                        not null comment '角色名称',
    remark         varchar(128)                       null comment '备注',
    create_user_id int                                not null comment '创建者id',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '角色';
