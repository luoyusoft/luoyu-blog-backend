package com.luoyu.blog.service.file;

import com.luoyu.blog.entity.file.vo.FileResourceVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 云存储资源表 服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
public interface MinioService {

    /**
     * 上传
     * @param file
     * @param fileModule
     */
    FileResourceVO upload(MultipartFile file, Integer fileModule);

    /**
     * 下载
     * @param response
     * @param fileName
     */
    void download(HttpServletResponse response, String fileName);

    /**
     * 获取下载地址
     * @param fileName
     */
    String getUrl(String fileName);

}
