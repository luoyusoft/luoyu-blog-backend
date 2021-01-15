package com.luoyu.blog.common.util;

import com.luoyu.blog.common.config.MinioProperties;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.entity.file.minio.MinioItem;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author top
 */
@Component
public class MinioUtils {

    @Autowired
    private MinioProperties properties;

    @Autowired
    private MinioClient minioClient;

    /**
     * 检查存储桶是否存在
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExists(String bucketName){
        try {
            return minioClient.bucketExists(bucketName);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_BUCKET_EXISTS_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 创建存储桶
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean createBucket(String bucketName) {
        try {
            if (!this.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
            return true;
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_CREATE_BUCKET_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 根据存储桶名称获取信息
     * @param bucketName 存储桶名称
     * @return
     */
    public Optional<Bucket> getBucket(String bucketName) {
        try {
            return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_BUCKET_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 根据bucketName删除信息
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(bucketName);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_DELETE_BUCKET_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 根据文件前缀查询文件
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public List<MinioItem> getMinioItemsByPrefix(String bucketName, String prefix, boolean recursive) {
        try {
            List<MinioItem> objectList = new ArrayList<>();
            Iterable<Result<Item>> objectsIterator = minioClient.listObjects(bucketName, prefix, recursive);
            for (Result<Item> result : objectsIterator) {
                objectList.add(new MinioItem(result.get()));
            }
            return objectList;
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_FILE_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取文件外链地址
     * @param bucketName 存储桶名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return String
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) {
        try {
            return minioClient.presignedGetObject(bucketName, objectName, expires);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_URL_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取文件外链
     * @param bucketName 存储桶名称
     * @param objectName 文件名称
     * @return url
     */
    public String getPresignedObjectUrl(String bucketName, String objectName) {
        try {
            return minioClient.presignedGetObject(bucketName, objectName);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_URL_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String bucketName, String objectName) {
        try {
            return minioClient.getObject(bucketName, objectName);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_FILE_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_BUCKET_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 文件上传
     * @param bucketName bucketName
     */
    public void upload(InputStream inputStream, String objectName, String bucketName, String contentType) {
        try {
            // 检查存储桶是否已经存在
            if (!this.bucketExists(bucketName)) {
                // 创建一个名为test的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }
            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(bucketName, objectName, inputStream, inputStream.available(), contentType);
            //关闭
            inputStream.close();
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_UPLOAD_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        try {
            minioClient.putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_UPLOAD_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) {
        try {
            minioClient.putObject(bucketName, objectName, stream, size, contextType);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_UPLOAD_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 下载
     *
     * @param response response
     * @param fileName fileName
     */
    public void download(HttpServletResponse response, String bucketName, String fileName) {
        InputStream inputStream = null;
        try {
            ObjectStat stat = minioClient.statObject(bucketName, fileName);
            inputStream = minioClient.getObject(bucketName, fileName);
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_DOWNLOAD_ERROR.getCode(), e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件url
     * @param objectName objectName
     * @return url
     */
    public String getObjectUrl(String bucketName, String objectName) {
        try {
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_URL_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取所有
     */
    public List<MinioItem> list(String bucketName) {
        try {
            List<MinioItem> list = new ArrayList<MinioItem>();
            Iterable<Result<Item>> results = minioClient.listObjects(bucketName);
            for (Result<Item> result : results) {
                Item item = result.get();
                MinioItem minioItem = new MinioItem(item);
                minioItem.setUrl(minioClient.getObjectUrl(bucketName, item.objectName()));
                list.add(minioItem);
            }
            return list;
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_GET_FILE_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 文件删除
     * @param fileName 文件名
     */
    public void delete(String bucketName, String fileName) {
        try {
            minioClient.removeObject(bucketName, fileName);
        } catch (Exception e) {
            throw new MyException(ResponseEnums.MINIO_DELETE_FILE_ERROR.getCode(), e.getMessage());
        }
    }

}
