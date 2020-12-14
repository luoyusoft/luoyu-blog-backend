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

    @ApiModelProperty(value = "文件所属模块（0：article，1：video，2：link）")
    private Integer fileModule;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "url")
    private String url;

}
