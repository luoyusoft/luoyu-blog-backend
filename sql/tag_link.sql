create table tag_link
(
    id          int auto_increment comment '主键'
        primary key,
    tag_id      int                                not null comment '标签id',
    link_id     int                                not null comment '标签链接id',
    module      int                                not null comment '所属模块',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '标签多对多维护表';
