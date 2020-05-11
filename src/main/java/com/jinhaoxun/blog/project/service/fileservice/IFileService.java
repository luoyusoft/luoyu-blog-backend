package com.jinhaoxun.blog.project.tt.service.fileservice;

import com.jinhaoxun.blog.project.tt.pojo.uploaddownloadfile.DownloadFileReq;
import com.jinhaoxun.blog.project.tt.pojo.uploaddownloadfile.UploadFileReq;
import com.jinhaoxun.blog.common.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 上传下载文件服务接口
 */
public interface IFileService {

    /**
     * @author jinhaoxun
     * @description 上传文件
     * @param uploadFileReq 上传文件参数
     * @return ResponseResult 上传结果
     * @throws Exception
     */
    ResponseResult uploadFile(UploadFileReq uploadFileReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 下载文件
     * @param downloadFileReq 下载文件参数
     * @return ResponseResult 下载结果
     * @throws Exception
     */
    ResponseResult downloadFile(DownloadFileReq downloadFileReq, HttpServletResponse httpServletResponse) throws Exception;

    /**
     * @author jinhaoxun
     * @description 解析Excel
     * @param multipartFile 要解析的文件
     * @return ResponseResult 解析后的数据
     * @throws Exception
     */
    ResponseResult resolveExcel(MultipartFile multipartFile) throws Exception;

    /**
     * @author jinhaoxun
     * @description 导出Excel
     * @return ResponseResult 创建结果
     * @throws Exception
     */
    ResponseResult createExcel() throws Exception;

}
