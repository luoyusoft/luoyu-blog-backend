package com.luoyu.blog.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 云存储资源表 服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
public interface FileResourceService extends IService<FileResource> {

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

    /**
     * 分页查询文件
     * @param page
     * @param limit
     * @param module
     */
    PageUtils queryPage(Integer page, Integer limit, Integer module);

    /**
     * 分片上传文件
     * @param fileResourceVO
     */
    List<FileResourceVO> chunkUpload(FileResourceVO fileResourceVO);

    /**
     * 分片上传，单个分片成功
     * @param fileResourceVO
     */
    Boolean chunkSuccess(FileResourceVO fileResourceVO);

    /**
     * 合并文件并返回文件信息
     * @param fileResourceVO
     */
    String composeFile(FileResourceVO fileResourceVO);

}
