create table video
(
    id                int auto_increment comment '主键'
        primary key,
    title             varchar(128)                         null comment '视频标题',
    alternate_name    varchar(128)                         null comment '视频又名',
    cover             varchar(1024)                        null comment '封面',
    video_url         varchar(1024)                        null comment '视频地址',
    author            varchar(64)                          null comment '上传者',
    category_id       varchar(64)                          null comment '视频分类类别（存在多级分类，用逗号隔开）',
    production_region varchar(128)                         null comment '制片国家/地区',
    director          varchar(128)                         null comment '导演',
    release_time      datetime                             null comment '上映日期',
    duration          varchar(64)                          null comment '片长（格式：HH:mm:ss）',
    language          varchar(64)                          null comment '语言',
    to_star           varchar(256)                         null comment '主演',
    score             varchar(64)                          null comment '评分',
    screenwriter      varchar(128)                         null comment '编剧',
    synopsis          varchar(2048)                        null comment '剧情简介',
    publish           tinyint(1) default 0                 null comment '是否发布',
    watch_num         int        default 0                 not null comment '观看量',
    like_num          int        default 0                 not null comment '点赞量',
    create_time       datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime   default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '视频表';
