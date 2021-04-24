create table file_chunk
(
    id            int auto_increment comment '主键'
        primary key,
    file_md5      varchar(256)                       null comment '文件的md5',
    upload_url    varchar(1024)                      null comment '上传url地址',
    upload_status tinyint(1)                         null comment '上传状态（0：未上传，1：已上传）',
    chunk_number  int                                null comment '分片序号',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '更新时间'
) engine InnoDB default charset utf8mb4 comment '云存储分片表';
