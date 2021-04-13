create table category
(
    id          int auto_increment comment '主键',
    name        varchar(128)                       not null comment '名称',
    module      int                                not null comment '模块',
    `rank`      int                                not null comment '级别',
    parent_id   int                                not null comment '父主键',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    constraint operation_category_id_uindex
        unique (id)
)
    comment '类别';

alter table category
    add primary key (id);
