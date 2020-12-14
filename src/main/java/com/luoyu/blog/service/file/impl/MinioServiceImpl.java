package com.luoyu.blog.service.file.impl;

import com.luoyu.blog.common.enums.FileModuleEnum;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.common.util.MinioUtils;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import com.luoyu.blog.service.file.FileResourceService;
import com.luoyu.blog.service.file.MinioService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 云存储资源表 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private FileResourceService fileResourceService;

    @Override
    public FileResourceVO upload(MultipartFile file, Integer fileModule) {
        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            String contentType = file.getContentType();
            String patchName = this.getPath() + suffix;
            String storageType = FileResource.STORAGE_TYPE_MINIO;
            String bucketName;
            if (suffix.equals(".mp4")){
                bucketName = FileResource.BUCKET_NAME_VIDEO;
            }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
                bucketName = FileResource.BUCKET_NAME_IMG;
            }else {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "不存在该文件类型");
            }

            FileResource fileResource = new FileResource();
            if (fileModule.equals(FileModuleEnum.ARTICLE.getCode())){
                fileResource.setFileModule(FileModuleEnum.ARTICLE.getName());
            }else if (fileModule.equals(FileModuleEnum.VIDEO.getCode())){
                fileResource.setFileModule(FileModuleEnum.VIDEO.getName());
            }else if (fileModule.equals(FileModuleEnum.LINK.getCode())){
                fileResource.setFileModule(FileModuleEnum.LINK.getName());
            }else {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "不存在该模块");
            }

            minioUtils.upload(inputStream, patchName, bucketName, contentType);
            String url = minioUtils.getObjectUrl(bucketName, patchName);
            url = url.replace("http://39.108.255.214:9090", "https://luoyublog.com/file");
            fileResource.setFileName(fileName);
            fileResource.setBucketName(bucketName);
            fileResource.setStorageType(storageType);
            fileResource.setUrl(url);
            fileResourceService.save(fileResource);
            FileResourceVO fileResourceVO = new FileResourceVO();
            fileResourceVO.setName(fileName);
            fileResourceVO.setUrl(url);
            return fileResourceVO;
        }catch (Exception e){
            throw new MyException(ResponseEnums.MINIO_UPLOAD_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 文件路径
     * @return 返回上传路径
     */
    private String getPath() {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        return DateUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;
    }

    @Override
    public void download(HttpServletResponse response, String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String bucketName;
        if (suffix.equals(".mp4")){
            bucketName = FileResource.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = FileResource.BUCKET_NAME_IMG;
        }else {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "不存在该文件类型");
        }

        minioUtils.download(response, bucketName, fileName);
    }

    @Override
    public String getUrl(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String bucketName;
        if (suffix.equals(".mp4")){
            bucketName = FileResource.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = FileResource.BUCKET_NAME_IMG;
        }else {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "不存在该文件类型");
        }

        return minioUtils.getObjectUrl(bucketName, fileName);
    }

}
