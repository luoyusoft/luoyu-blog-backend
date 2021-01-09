package com.luoyu.blog.entity.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@EqualsAndHashCode(callSuper = false)
@TableName("file_resource")
public class FileResource extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String BUCKET_NAME_IMG = "img";

    public static final String BUCKET_NAME_VIDEO = "video";

    public static final String STORAGE_TYPE_QINIUYUN = "qiniuyun";

    public static final String STORAGE_TYPE_MINIO = "minio";

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "存储类型（qiniuyun，minio）")
    private String storageType;

    @ApiModelProperty(value = "桶名")
    private String bucketName;

    @ApiModelProperty(value = "文件所属模块（article，video，link）")
    private Integer module;

}
