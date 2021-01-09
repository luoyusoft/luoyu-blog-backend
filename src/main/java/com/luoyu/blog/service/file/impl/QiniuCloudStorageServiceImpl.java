package com.luoyu.blog.service.file.impl;

import com.luoyu.blog.common.config.CloudStorageProperties;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import com.luoyu.blog.service.file.CloudStorageService;
import com.luoyu.blog.service.file.FileResourceService;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * QiniuCloudStorageService
 *
 * @author luoyu
 * @date 2018/10/22 12:35
 * @description
 */
@Service("cloudStorageService")
@Slf4j
public class QiniuCloudStorageServiceImpl extends CloudStorageService {

    @Autowired
    private CloudStorageProperties cloudStorageProperties;

    @Autowired
    private FileResourceService fileResourceService;

    private UploadManager uploadManager;
    private String token;
    private Auth auth;

    public QiniuCloudStorageServiceImpl(CloudStorageProperties config){
        this.config = config;
        //初始化
        init();
    }

    private void init(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        auth = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey());
        token = auth.uploadToken(config.getQiniuBucketName());
    }

    @Override
    public FileResourceVO upload(MultipartFile file, Integer fileModule) {
        try {
            //上传文件
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String url = this.uploadSuffix(file.getBytes(), suffix);
            FileResource fileResource = new FileResource();
            fileResource.setModule(fileModule);
            fileResource.setFileName(fileName);
            fileResource.setBucketName(cloudStorageProperties.getQiniuBucketName());
            fileResource.setStorageType(FileResource.STORAGE_TYPE_QINIUYUN);
            fileResource.setUrl(url);
            fileResourceService.save(fileResource);
            FileResourceVO fileResourceVO = new FileResourceVO();
            fileResourceVO.setName(fileName);
            fileResourceVO.setUrl(url);
            return fileResourceVO;
        }catch (Exception e){
            throw new MyException(ResponseEnums.OSS_UPLOAD_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    private String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;
        if(StringUtils.isNotBlank(prefix)){
            path = prefix + "/" + path;
        }

        return path + suffix;
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            token = auth.uploadToken(config.getQiniuBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResponseEnums.OSS_CONFIG_ERROR.getCode(), e.getMessage());
        }

        return config.getQiniuDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ResponseEnums.OSS_CONFIG_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, this.getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, this.getPath(config.getQiniuPrefix(), suffix));
    }

}
