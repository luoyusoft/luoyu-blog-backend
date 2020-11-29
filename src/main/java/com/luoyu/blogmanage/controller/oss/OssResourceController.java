package com.luoyu.blogmanage.controller.oss;

import com.luoyu.blogmanage.common.enums.ResponseEnums;
import com.luoyu.blogmanage.entity.base.Response;
import com.luoyu.blogmanage.entity.oss.OssResource;
import com.luoyu.blogmanage.common.exception.MyException;
import com.luoyu.blogmanage.service.oss.CloudStorageService;
import com.luoyu.blogmanage.service.oss.OssResourceService;
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
@RequestMapping("/admin/oss/resource")
public class OssResourceController {

    @Autowired
    private OssResourceService ossResourceService;

    @Autowired
    private CloudStorageService cloudStorageService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
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
