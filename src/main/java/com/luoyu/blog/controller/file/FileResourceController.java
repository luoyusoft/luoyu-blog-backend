package com.luoyu.blog.controller.file;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import com.luoyu.blog.service.file.CloudStorageService;
import com.luoyu.blog.service.file.FileResourceService;
import com.luoyu.blog.service.file.MinioService;
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
    private MinioService minioService;

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

        return Response.success(minioService.upload(fileResourceVO.getFile(), fileResourceVO.getModule()));
    }

    /**
     * 下载文件
     */
    @PostMapping("/manage/file/resource/minio/download")
    public Response downloadByMinio(HttpServletResponse response, @RequestBody FileResourceVO fileResourceVO) throws Exception {
        minioService.download(response, fileResourceVO.getName());
        return Response.success();
    }

    /**
     * 获取文件下载地址
     */
    @GetMapping("/manage/file/resource/minio/url")
    public Response getUrlByMinio(@RequestParam("fileName") String fileName) throws Exception {
        return Response.success(minioService.getUrl(fileName));
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

}
