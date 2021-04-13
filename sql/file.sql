create table file
(
    id            int auto_increment comment '主键'
        primary key,
    file_name     varchar(256)                       not null comment '文件名称',
    url           varchar(1024)                      null comment 'url地址',
    storage_type  varchar(64)                        not null comment '存储类型（qiniuyun，minio）',
    bucket_name   varchar(64)                        not null comment '桶名',
    module        int                                null comment '文件所属模块',
    file_md5      varchar(256)                       null comment '文件的md5',
    file_size     varchar(64)                        null comment '文件大小',
    suffix        varchar(64)                        null comment '文件格式',
    is_chunk      tinyint(1)                         null comment '是否分片（0：否，1：是）',
    chunk_count   int                                null comment '分片总数量',
    upload_status tinyint(1)                         null comment '上传状态（0：部分成功，1：成功）',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '云存储资源表';
