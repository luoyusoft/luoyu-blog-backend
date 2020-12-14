package com.luoyu.blog.entity.file;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 云存储资源表
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Data
@ApiModel(value="FileResource对象", description="云存储资源表")
@TableName("file_resource")
public class FileResource implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String BUCKET_NAME_IMG = "img";

    public static final String BUCKET_NAME_VIDEO = "video";

    public static final String STORAGE_TYPE_QINIUYUN = "qiniuyun";

    public static final String STORAGE_TYPE_MINIO = "minio";

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "存储类型（qiniuyun，minio）")
    private String storageType;

    @ApiModelProperty(value = "桶名")
    private String bucketName;

    @ApiModelProperty(value = "文件所属模块（article，video，link）")
    private String fileModule;

    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date, format = DateFormat.none)
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
