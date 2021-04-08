package com.luoyu.blog.controller.file;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import com.luoyu.blog.service.file.CloudStorageService;
import com.luoyu.blog.service.file.FileResourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 云存储资源表 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@RestController
public class FileResourceController {

    @Autowired
    private CloudStorageService cloudStorageService;

    @Autowired
    private FileResourceService fileResourceService;

    /**
     * 上传文件
     */
    @PostMapping("/manage/file/resource/qiniuyun/upload")
    public Response uploadByQiNiuYun(FileResourceVO fileResourceVO) throws Exception {
        if (fileResourceVO.getFile() == null || fileResourceVO.getFile().isEmpty()
                || fileResourceVO.getModule() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件，文件所属模块不能为空");
        }
        return Response.success(cloudStorageService.upload(fileResourceVO.getFile(), fileResourceVO.getModule()));
    }

    /**
     * 上传文件
     */
    @PostMapping("/manage/file/resource/minio/upload")
    public Response uploadByMinio(FileResourceVO fileResourceVO) throws Exception {
        if (fileResourceVO.getFile() == null || fileResourceVO.getFile().isEmpty()
                || fileResourceVO.getModule() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件，文件所属模块不能为空");
        }
        return Response.success(fileResourceService.upload(fileResourceVO.getFile(), fileResourceVO.getModule()));
    }

    /**
     * 分片上传文件
     */
    @PostMapping("/manage/file/resource/minio/chunkUpload")
    public Response chunkUpload(FileResourceVO fileResourceVO) throws Exception {
        if (fileResourceVO.getFile() == null || fileResourceVO.getFile().isEmpty()
                || StringUtils.isEmpty(fileResourceVO.getBucketName()) || StringUtils.isEmpty(fileResourceVO.getFileMd5())
                || fileResourceVO.getChunkNumber() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件，桶名，文件的md5，分片序号不能为空");
        }
        fileResourceService.chunkUpload(fileResourceVO.getFile(), fileResourceVO.getBucketName(), fileResourceVO.getFileMd5(), fileResourceVO.getChunkNumber());
        return Response.success();
    }

    /**
     * 下载文件
     */
    @PostMapping("/manage/file/resource/minio/download")
    public Response downloadByMinio(HttpServletResponse response, @RequestBody FileResourceVO fileResourceVO) throws Exception {
        fileResourceService.download(response, fileResourceVO.getFileName());
        return Response.success();
    }

    /**
     * 获取列表
     */
    @GetMapping("/manage/file/resource/list")
    @RequiresPermissions("file:list")
    public Response listTimeline(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("module") Integer module) {
        PageUtils logViewPage = fileResourceService.queryPage(page, limit, module);
        return Response.success(logViewPage);
    }

    /**
     * 分片上传文件，获取各个分片上传地址
     */
    @PostMapping("/manage/file/resource/minio/chunk")
    public Response chunk(@RequestBody FileResourceVO fileResourceVO){
        if (StringUtils.isEmpty(fileResourceVO.getFileMd5()) || StringUtils.isEmpty(fileResourceVO.getFileName())
                || fileResourceVO.getModule() == null || fileResourceVO.getChunkCount() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，文件名称，文件所属模块，分片总数量不能为空");
        }
        return Response.success(fileResourceService.chunk(fileResourceVO));
    }

    /**
     * 分片上传，单个分片成功更新
     */
    @PutMapping("/manage/file/resource/minio/chunkUploadSuccess")
    public Response chunkUploadSuccess(@RequestBody FileResourceVO fileResourceVO){
        if (StringUtils.isEmpty(fileResourceVO.getFileMd5()) || fileResourceVO.getChunkNumber() == null
                || fileResourceVO.getModule() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，当前分片，文件所属模块不能为空");
        }
        return Response.success(fileResourceService.chunkUploadSuccess(fileResourceVO));
    }

    /**
     * 合并文件，获取文件访问地址
     */
    @PostMapping("/manage/file/resource/minio/compose")
    public Response composeFile(@RequestBody FileResourceVO fileResourceVO){
        if (StringUtils.isEmpty(fileResourceVO.getFileMd5()) || StringUtils.isEmpty(fileResourceVO.getFileName())
                || fileResourceVO.getModule() == null || fileResourceVO.getChunkCount() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，文件名称，文件所属模块，分片总数量不能为空");
        }

        return Response.success(fileResourceService.composeFile(fileResourceVO));
    }

    /**
     * 获取文件访问地址
     */
    @GetMapping("/manage/file/resource/minio/url")
    public Response getFileUrl(@RequestParam("fileMd5") String fileMd5, @RequestParam("module") Integer module){
        if (StringUtils.isEmpty(fileMd5) || module == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "文件md5，文件所属模块不能为空");
        }

        return Response.success(fileResourceService.getFileUrl(fileMd5, module));
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/manage/file/resource/minio/file")
    public Response deleteFile(@RequestBody Integer[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        fileResourceService.deleteFile(ids);
        return Response.success();
    }

}
