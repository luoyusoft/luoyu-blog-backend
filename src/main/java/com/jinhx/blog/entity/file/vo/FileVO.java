package com.jinhx.blog.entity.file.vo;

import com.jinhx.blog.entity.file.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Data
@ApiModel(value="FileVO对象", description="文件表")
public class FileVO extends File {

    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "分片序号")
    private Integer chunkNumber;

    @ApiModelProperty(value = "上传url地址")
    private String uploadUrl;

}
