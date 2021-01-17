package com.luoyu.blog.entity.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
@ApiModel(value="FileResourceVO对象", description="云存储资源表")
public class FileResourceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "桶名")
    private String bucketName;

    @ApiModelProperty(value = "文件所属模块（article，video，link）")
    private Integer module;

    @ApiModelProperty(value = "文件的md5")
    private String fileMd5;

    @ApiModelProperty(value = "是否分片（0：否，1：是）")
    private Integer isChunk;

    @ApiModelProperty(value = "分片总数量")
    private Integer chunkCount;

    @ApiModelProperty(value = "当前分片")
    private Integer chunk;

    @ApiModelProperty(value = "上传url地址")
    private String uploadUrl;

}
