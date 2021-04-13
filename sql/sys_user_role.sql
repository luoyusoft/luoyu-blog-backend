create table sys_user_role
(
    id          int auto_increment comment '主键'
        primary key,
    user_id     int                                not null comment '用户id',
    role_id     int                                not null comment '角色id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '用户与角色对应关系';
