create table sys_menu
(
    id          int auto_increment comment '主键'
        primary key,
    parent_id   int                                not null comment '父菜单id，一级菜单为0',
    name        varchar(128)                       not null comment '菜单名称',
    url         varchar(256)                       null comment '菜单url',
    perms       varchar(1024)                      null comment '授权(多个用逗号隔开，如：user:list,user:create)',
    type        int                                not null comment '类型（0：目录，1：菜单，2：按钮）',
    icon        varchar(128)                       null comment '菜单图标',
    order_num   int                                not null comment '排序',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '菜单';
