package com.luoyu.blog.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.common.util.MinioUtils;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import com.luoyu.blog.mapper.file.FileResourceMapper;
import com.luoyu.blog.service.file.FileResourceService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 云存储资源表 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Service
public class FileResourceServiceImpl extends ServiceImpl<FileResourceMapper, FileResource> implements FileResourceService {

    @Autowired
    private MinioUtils minioUtils;

    /**
     * 上传
     * @param file
     * @param fileModule
     */
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
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
            }

            FileResource fileResource = new FileResource();
            fileResource.setModule(fileModule);

            minioUtils.upload(inputStream, patchName, bucketName, contentType);
            String url = minioUtils.getObjectUrl(bucketName, patchName);
            url = url.replace("http://39.108.255.214:9090", "https://luoyublog.com/file");
            fileResource.setFileName(fileName);
            fileResource.setBucketName(bucketName);
            fileResource.setStorageType(storageType);
            fileResource.setUrl(url);
            baseMapper.insert(fileResource);
            FileResourceVO fileResourceVO = new FileResourceVO();
            fileResourceVO.setFileName(fileName);
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

    /**
     * 下载
     * @param response
     * @param fileName
     */
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

    /**
     * 获取下载地址
     * @param fileName
     */
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

    /**
     * 分页查询文件
     * @param page
     * @param limit
     * @param module
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, Integer module) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));

        IPage<FileResource> fileResourceIPage = baseMapper.selectPage(new Query<FileResource>(params).getPage(),
                new QueryWrapper<FileResource>().lambda()
                        .eq(module != null, FileResource::getModule,module)
                        .orderByDesc(FileResource::getCreateTime)
        );
        return new PageUtils(fileResourceIPage);
    }

    /**
     * 分片上传文件
     * @param fileResourceVO
     */
    @Override
    public List<FileResourceVO> chunkUpload(FileResourceVO fileResourceVO) {
        String bucketName;
        FileResource fileResource = baseMapper.selectFileResourceByFileMd5AndModule(fileResourceVO.getFileMd5(), fileResourceVO.getModule());
        // 校验该文件是否上传过
        if(fileResource != null){
            // 秒传
            if(fileResource.getUploadStatus().equals(FileResource.UPLOAD_STATUS_1)){
                return Collections.emptyList();
            }
            // 续传
            if (fileResource.getSuffix().equals(".mp4")){
                bucketName = FileResource.BUCKET_NAME_VIDEO;
            }else if (fileResource.getSuffix().equals(".gif") || fileResource.getSuffix().equals(".jpg") || fileResource.getSuffix().equals(".png")){
                bucketName = FileResource.BUCKET_NAME_IMG;
            }else {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
            }
            Map<Integer, String> successChunkMap = minioUtils.mapChunkObjectNames(bucketName, fileResource.getFileMd5(), true);
            List<FileResourceVO> fileResourceVOList = new ArrayList<>();

            List<Integer> chunkUploadUrls = new ArrayList<>();
            if (!StringUtils.isEmpty(fileResource.getUploadedChunk())){
                chunkUploadUrls = Arrays.asList((Integer[]) ConvertUtils.convert(fileResource.getUploadedChunk().split(","), Integer.class));
            }

            if (!CollectionUtils.isEmpty(successChunkMap)){
                List<Integer> keys = new ArrayList<>(successChunkMap.keySet());
                keys.removeAll(chunkUploadUrls);
                keys.forEach(keysItem -> {
                    FileResourceVO file = new FileResourceVO();
                    file.setUploadUrl(successChunkMap.get(keysItem));
                    file.setChunk(keysItem);
                    fileResourceVOList.add(file);
                });

                return fileResourceVOList;
            }
        }
        // 初次上传和已有文件信息，但未上传任何分片的情况下则直接生成所有上传url
        String suffix = fileResourceVO.getFileName().substring(fileResourceVO.getFileName().lastIndexOf("."));
        if (suffix.equals(".mp4")){
            bucketName = FileResource.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = FileResource.BUCKET_NAME_IMG;
        }else {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
        }
        List<String> uploadUrls = minioUtils.createUploadChunkUrlList(bucketName, fileResourceVO.getFileMd5(), fileResourceVO.getChunkCount(), 604800);
        List<FileResourceVO> fileResourceVOList = new ArrayList<>();
        for (int i = 1; i <= uploadUrls.size(); i++) {
            FileResourceVO file = new FileResourceVO();
            file.setUploadUrl(minioUtils.createUploadChunkUrl(bucketName, fileResourceVO.getFileMd5(), i, 604800));
            file.setChunk(i);
            fileResourceVOList.add(file);
        }
        // 向数据库中记录该文件的上传信息
        FileResource newFileResource = new FileResource();
        newFileResource.setFileName(fileResourceVO.getFileName());
        newFileResource.setFileMd5(fileResourceVO.getFileMd5());
        newFileResource.setBucketName(bucketName);
        newFileResource.setIsChunk(FileResource.IS_CHUNK_1);
        newFileResource.setStorageType(FileResource.STORAGE_TYPE_MINIO);
        newFileResource.setModule(fileResourceVO.getModule());
        newFileResource.setSuffix(suffix);
        newFileResource.setChunkCount(fileResourceVO.getChunkCount());
        newFileResource.setUploadStatus(FileResource.IS_CHUNK_0);
        baseMapper.insert(newFileResource);

        return fileResourceVOList;
    }

    /**
     * 合并文件并返回文件信息
     * @param fileResourceVO
     */
    @Override
    public String composeFile(FileResourceVO fileResourceVO) {
        String bucketName;
        String suffix = fileResourceVO.getFileName().substring(fileResourceVO.getFileName().lastIndexOf("."));
        if (suffix.equals(".mp4")){
            bucketName = FileResource.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = FileResource.BUCKET_NAME_IMG;
        }else {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
        }
        // 根据md5获取所有分片文件名称(minio的文件名称 = 文件path)
        List<String> chunks = minioUtils.listObjectNames(bucketName, fileResourceVO.getFileMd5(), true);

        // 自定义文件名称
        String patchName = this.getPath() + suffix;

        // 合并文件
        if(minioUtils.composeObject(bucketName, bucketName, chunks, patchName)){
            // 获取文件访问外链(1小时过期)
            String url = minioUtils.getObjectUrl(bucketName, patchName);
            url = url.replace("http://39.108.255.214:9090", "https://luoyublog.com/file");
            // 获取数据库里记录的文件信息，修改数据并返回文件信息
            FileResource fileResource = new FileResource();
            fileResource.setFileMd5(fileResourceVO.getFileMd5());
            fileResource.setModule(fileResourceVO.getModule());
            fileResource.setUrl(url);
            fileResource.setUploadStatus(FileResource.UPLOAD_STATUS_1);
            fileResource.setUpdateTime(LocalDateTime.now());
            baseMapper.updateFileResourceByFileMd5AndModule(fileResource);
            return url;
        }
        return null;
    }

}
