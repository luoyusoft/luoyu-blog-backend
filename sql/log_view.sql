create table log_view
(
    id                  bigint auto_increment comment '主键'
        primary key,
    module              int                                null comment '浏览模块',
    method              varchar(256)                       null comment '请求方法',
    headr_params        varchar(2048)                      null comment '请求头参数',
    time                bigint                             null comment '执行时长(毫秒)',
    ip                  varchar(64)                        null comment 'ip地址',
    country             varchar(64)                        null comment '国家',
    region              varchar(64)                        null comment '省份',
    city                varchar(64)                        null comment '城市',
    border_name         varchar(64)                        null comment '浏览器名称',
    border_version      varchar(64)                        null comment '浏览器版本',
    device_manufacturer varchar(64)                        null comment '设备生产厂商',
    device_type         varchar(64)                        null comment '设备类型',
    os_version          varchar(64)                        null comment '操作系统的版本号',
    uri                 varchar(128)                       null comment '请求uri',
    request_type        varchar(64)                        null comment '请求类型（GET，POST，DELETE等）',
    body_params         text                               null comment '请求体参数',
    response            text                               null comment '响应结果',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '访问记录';
