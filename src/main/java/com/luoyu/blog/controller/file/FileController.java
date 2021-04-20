package com.luoyu.blog.controller.file;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.file.vo.FileVO;
import com.luoyu.blog.service.file.CloudStorageService;
import com.luoyu.blog.service.file.FileService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件表 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@RestController
public class FileController {

    @Autowired
    private CloudStorageService cloudStorageService;

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     */
    @PostMapping("/manage/file/qiniuyun/upload")
    public Response uploadByQiNiuYun(FileVO fileVO) throws Exception {
        if (fileVO.getFile() == null || fileVO.getFile().isEmpty()
                || fileVO.getModule() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件，文件所属模块不能为空");
        }
        return Response.success(cloudStorageService.upload(fileVO.getFile(), fileVO.getModule()));
    }

    /**
     * 上传文件
     */
    @PostMapping("/manage/file/minio/upload")
    public Response uploadByMinio(FileVO fileVO) throws Exception {
        if (fileVO.getFile() == null || fileVO.getFile().isEmpty()
                || fileVO.getModule() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件，文件所属模块不能为空");
        }
        return Response.success(fileService.upload(fileVO.getFile(), fileVO.getModule()));
    }

    /**
     * 分片上传文件
     */
    @PostMapping("/manage/file/minio/chunkUpload")
    public Response chunkUpload(FileVO fileVO) throws Exception {
        if (fileVO.getFile() == null || fileVO.getFile().isEmpty()
                || StringUtils.isEmpty(fileVO.getBucketName()) || StringUtils.isEmpty(fileVO.getFileMd5())
                || fileVO.getChunkNumber() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件，桶名，文件的md5，分片序号不能为空");
        }
        fileService.chunkUpload(fileVO.getFile(), fileVO.getBucketName(), fileVO.getFileMd5(), fileVO.getChunkNumber());
        return Response.success();
    }

    /**
     * 下载文件
     */
    @PostMapping("/manage/file/minio/download")
    public Response downloadByMinio(HttpServletResponse response, @RequestBody FileVO fileVO) throws Exception {
        fileService.download(response, fileVO.getFileName());
        return Response.success();
    }

    /**
     * 获取列表
     */
    @GetMapping("/manage/file/list")
    @RequiresPermissions("file:list")
    public Response listTimeline(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("module") Integer module,
                                 @RequestParam("fileName") String fileName, @RequestParam("fileMd5") String fileMd5, @RequestParam("url") String url) {
        PageUtils logViewPage = fileService.queryPage(page, limit, module, fileName, fileMd5, url);
        return Response.success(logViewPage);
    }

    /**
     * 分片上传文件，获取各个分片上传地址
     */
    @PostMapping("/manage/file/minio/chunk")
    public Response chunk(@RequestBody FileVO fileVO){
        if (StringUtils.isEmpty(fileVO.getFileMd5()) || StringUtils.isEmpty(fileVO.getFileName())
                || fileVO.getModule() == null || fileVO.getChunkCount() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，文件名称，文件所属模块，分片总数量不能为空");
        }
        return Response.success(fileService.chunk(fileVO));
    }

    /**
     * 分片上传，单个分片成功更新
     */
    @PutMapping("/manage/file/minio/chunkUploadSuccess")
    public Response chunkUploadSuccess(@RequestBody FileVO fileVO){
        if (StringUtils.isEmpty(fileVO.getFileMd5()) || fileVO.getChunkNumber() == null
                || fileVO.getModule() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，当前分片，文件所属模块不能为空");
        }
        return Response.success(fileService.chunkUploadSuccess(fileVO));
    }

    /**
     * 合并文件，获取文件访问地址
     */
    @PostMapping("/manage/file/minio/compose")
    public Response composeFile(@RequestBody FileVO fileVO){
        if (StringUtils.isEmpty(fileVO.getFileMd5()) || StringUtils.isEmpty(fileVO.getFileName())
                || fileVO.getModule() == null || fileVO.getChunkCount() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，文件名称，文件所属模块，分片总数量不能为空");
        }

        return Response.success(fileService.composeFile(fileVO));
    }

    /**
     * 获取文件访问地址
     */
    @GetMapping("/manage/file/minio/url")
    public Response getFileUrl(@RequestParam("fileMd5") String fileMd5, @RequestParam("module") Integer module){
        if (StringUtils.isEmpty(fileMd5) || module == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，文件所属模块不能为空");
        }

        return Response.success(fileService.getFileUrl(fileMd5, module));
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/manage/file/minio/file")
    @RequiresPermissions("file:delete")
    public Response deleteFile(@RequestBody Integer[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        fileService.deleteFile(ids);
        return Response.success();
    }

}
