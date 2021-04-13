create table top
(
    id          int auto_increment comment '主键'
        primary key,
    link_id     int                                not null comment '置顶链接id',
    module      int                                not null comment '置顶模块',
    order_num   int      default 0                 not null comment '顺序',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    constraint unidx_order_num
        unique (order_num),
    constraint unidx_top_link_id_type
        unique (link_id, module)
)
    comment '置顶';
