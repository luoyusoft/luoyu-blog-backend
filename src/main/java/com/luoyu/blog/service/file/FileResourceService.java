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
     * 分片上传
     * @param file
     * @param bucketName
     * @param fileMd5
     * @param chunkNumber
     */
    void chunkUpload(MultipartFile file, String bucketName, String fileMd5, Integer chunkNumber);

    /**
     * 下载
     * @param response
     * @param fileName
     */
    void download(HttpServletResponse response, String fileName);

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
    List<FileResourceVO> chunk(FileResourceVO fileResourceVO);

    /**
     * 分片上传，单个分片成功
     * @param fileResourceVO
     */
    Boolean chunkUploadSuccess(FileResourceVO fileResourceVO);

    /**
     * 合并文件并返回文件信息
     * @param fileResourceVO
     */
    String composeFile(FileResourceVO fileResourceVO);

    /**
     * 获取文件访问地址
     * @param fileMd5
     * @param module
     */
    String getFileUrl(String fileMd5, Integer module);

}
