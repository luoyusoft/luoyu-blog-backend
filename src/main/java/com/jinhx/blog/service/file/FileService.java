package com.jinhx.blog.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.entity.file.File;
import com.jinhx.blog.entity.file.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 文件表 服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
public interface FileService extends IService<File> {

    /**
     * 上传
     * @param file
     * @param fileModule
     */
    FileVO upload(MultipartFile file, Integer fileModule);

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
     * @param page page
     * @param limit limit
     * @param module module
     * @param fileName fileName
     * @param fileMd5 fileMd5
     * @param url url
     */
    PageUtils queryPage(Integer page, Integer limit, Integer module, String fileName, String fileMd5, String url);

    /**
     * 分片上传文件
     * @param fileVO
     */
    List<FileVO> chunk(FileVO fileVO);

    /**
     * 分片上传，单个分片成功
     * @param fileVO
     */
    Boolean chunkUploadSuccess(FileVO fileVO);

    /**
     * 合并文件并返回文件信息
     * @param fileVO
     */
    String composeFile(FileVO fileVO);

    /**
     * 获取文件访问地址
     * @param fileMd5
     * @param module
     */
    String getFileUrl(String fileMd5, Integer module);

    /**
     * 批量删除文件
     * @param ids
     */
    void deleteFile(Integer[] ids);

}
