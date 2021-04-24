create table tag
(
    id          int auto_increment comment '主键'
        primary key,
    name        varchar(64)                        not null comment '标签名字',
    module      int                                not null comment '所属模块',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '标签';
