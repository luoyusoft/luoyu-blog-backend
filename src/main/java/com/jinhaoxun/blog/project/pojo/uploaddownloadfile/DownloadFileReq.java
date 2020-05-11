package com.jinhaoxun.blog.project.tt.pojo.uploaddownloadfile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 下载文件请求实体类
 */
@Setter
@Getter
@ToString
public class DownloadFileReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "文件名不能为空")
    private String fileName;

    @NotNull(message = "文件类型不能为空")
    private int fileType;

}
