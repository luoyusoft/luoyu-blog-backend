package com.jinhaoxun.blog.project.tt.pojo.uploaddownloadfile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 上传文件请求实体类
 */
@Setter
@Getter
@ToString
public class UploadFileReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "上传文件不能为空")
    private MultipartFile[] multipartFile;

}
