package com.luoyu.blog.controller.oss;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.oss.OssResource;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.service.oss.CloudStorageService;
import com.luoyu.blog.service.oss.OssResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 云存储资源表 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@RestController
public class OssResourceController {

    @Autowired
    private OssResourceService ossResourceService;

    @Autowired
    private CloudStorageService cloudStorageService;

    /**
     * 上传文件
     */
    @PostMapping("/manage/oss/resource/upload")
    public Response uploadCover(MultipartFile file) throws Exception {
        if (file!=null && file.isEmpty()) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上传文件不能为空");
        }
        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url =cloudStorageService.uploadSuffix(file.getBytes(), suffix);
        OssResource resource=new OssResource(url,file.getOriginalFilename());
        ossResourceService.save(resource);
        return Response.success(resource);
    }

}
