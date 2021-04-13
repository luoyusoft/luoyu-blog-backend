create table sys_role_menu
(
    id          int auto_increment comment '主键'
        primary key,
    role_id     int                                not null comment '角色id',
    menu_id     int                                not null comment '菜单id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '角色与菜单对应关系';
