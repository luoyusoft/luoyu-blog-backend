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
import com.luoyu.blog.entity.file.FileChunk;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import com.luoyu.blog.mapper.file.FileResourceMapper;
import com.luoyu.blog.service.article.ArticleService;
import com.luoyu.blog.service.file.FileChunkService;
import com.luoyu.blog.service.file.FileResourceService;
import com.luoyu.blog.service.operation.LinkService;
import com.luoyu.blog.service.video.VideoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private FileResourceMapper fileResourceMapper;

    @Autowired
    private FileChunkService fileChunkService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private LinkService linkService;

    @Value("${minio.base.url}")
    private String minioBaseUrl;

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
            url = url.replace(minioBaseUrl, "https://minio.luoyublog.com").substring(0, url.indexOf("?") + 1);
            fileResource.setFileName(fileName);
            fileResource.setBucketName(bucketName);
            fileResource.setStorageType(storageType);
            fileResource.setUrl(url);
            fileResource.setIsChunk(FileResource.IS_CHUNK_0);
            fileResource.setChunkCount(0);
            fileResource.setUploadStatus(FileResource.UPLOAD_STATUS_1);
            fileResource.setSuffix(suffix);
            fileResource.setFileMd5(DigestUtils.md5Hex(file.getInputStream()));
            fileResource.setFileSize(this.getFileSize(file.getSize()));
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
     * 分片上传
     * @param file
     * @param bucketName
     * @param fileMd5
     * @param chunkNumber
     */
    @Override
    public void chunkUpload(MultipartFile file, String bucketName, String fileMd5, Integer chunkNumber) {
        try {
            minioUtils.upload(file.getInputStream(), fileMd5 + "/" + chunkNumber + ".chunk", bucketName, file.getContentType());
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
    public List<FileResourceVO> chunk(FileResourceVO fileResourceVO) {
        String bucketName = null;
        FileResource fileResource = fileResourceMapper.selectFileResourceByFileMd5AndModule(fileResourceVO.getFileMd5(), fileResourceVO.getModule());
        // 校验该文件是否上传过
        if(fileResource != null){
            // 秒传
            if(fileResource.getUploadStatus().equals(FileResource.UPLOAD_STATUS_1)){
                return Collections.emptyList();
            }
            // 续传
            List<FileChunk> fileChunks = fileChunkService.selectFileChunksByFileMd5(fileResource.getFileMd5());
            if (!CollectionUtils.isEmpty(fileChunks)){
                List<FileResourceVO> fileResourceVOList = new ArrayList<>();
                for (FileChunk fileChunk : fileChunks){
                    FileResourceVO file = new FileResourceVO();
                    file.setUploadUrl(fileChunk.getUploadUrl());
                    file.setChunkNumber(fileChunk.getChunkNumber());
                    file.setUploadStatus(fileChunk.getUploadStatus());
                    file.setFileMd5(fileChunk.getFileMd5());
                    file.setBucketName(bucketName);
                    fileResourceVOList.add(file);
                }

                return fileResourceVOList;
            }
        }
        // 初次上传
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
        for (int i = 0; i < uploadUrls.size(); i++) {
            FileResourceVO file = new FileResourceVO();
            String url = minioUtils.createUploadChunkUrl(bucketName, fileResourceVO.getFileMd5(), i, 604800);
            file.setUploadUrl(url);
            file.setChunkNumber(i);
            file.setUploadStatus(FileChunk.UPLOAD_STATUS_0);
            file.setFileMd5(fileResourceVO.getFileMd5());
            file.setBucketName(bucketName);
            fileResourceVOList.add(file);

            FileChunk fileChunk = new FileChunk();
            fileChunk.setFileMd5(fileResourceVO.getFileMd5());
            fileChunk.setUploadUrl(url);
            fileChunk.setUploadStatus(FileChunk.UPLOAD_STATUS_0);
            fileChunk.setChunkNumber(file.getChunkNumber());
            fileChunkService.insertFileChunk(fileChunk);
        }
        // 向数据库中记录该文件的上传信息
        FileResource newFileResource = new FileResource();
        newFileResource.setFileName(fileResourceVO.getFileName());
        newFileResource.setFileMd5(fileResourceVO.getFileMd5());
        newFileResource.setBucketName(bucketName);
        newFileResource.setFileSize(this.getFileSize(fileResourceVO.getFileSize()));
        newFileResource.setIsChunk(FileResource.IS_CHUNK_1);
        newFileResource.setStorageType(FileResource.STORAGE_TYPE_MINIO);
        newFileResource.setModule(fileResourceVO.getModule());
        newFileResource.setSuffix(suffix);
        newFileResource.setChunkCount(fileResourceVO.getChunkCount());
        newFileResource.setUploadStatus(FileResource.UPLOAD_STATUS_0);
        baseMapper.insert(newFileResource);

        return fileResourceVOList;
    }

    /**
     * 分片上传，单个分片成功
     * @param fileResourceVO
     */
    @Override
    public Boolean chunkUploadSuccess(FileResourceVO fileResourceVO) {
        FileResource fileResource = fileResourceMapper.selectFileResourceByFileMd5AndModule(fileResourceVO.getFileMd5(), fileResourceVO.getModule());
        if (fileResource == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该文件未上传过");
        }

        FileChunk fileChunk = new FileChunk();
        fileChunk.setFileMd5(fileResourceVO.getFileMd5());
        fileChunk.setUploadStatus(FileChunk.UPLOAD_STATUS_1);
        fileChunk.setChunkNumber(fileResourceVO.getChunkNumber());
        fileChunk.setUpdateTime(LocalDateTime.now());
        return fileChunkService.updateFileChunkByFileMd5AndChunkNumber(fileChunk);
    }

    /**
     * 合并文件并返回文件信息
     * @param fileResourceVO
     */
    @Override
    public String composeFile(FileResourceVO fileResourceVO) {
        if (fileResourceMapper.selectFileResourceByFileMd5AndModule(fileResourceVO.getFileMd5(), fileResourceVO.getModule()) == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该文件未上传过");
        }
        if(!fileChunkService.checkIsUploadAllChunkByFileMd5(fileResourceVO.getFileMd5())){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该文件还有部分分片未上传");
        }

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
            minioUtils.deleteObjectNames(bucketName, chunks);

            // 获取文件访问外链(1小时过期)
            String url = minioUtils.getObjectUrl(bucketName, patchName);
            url = url.replace(minioBaseUrl, "https://minio.luoyublog.com").substring(0, url.indexOf("?") + 1);
            // 获取数据库里记录的文件信息，修改数据并返回文件信息
            FileResource fileResource = new FileResource();
            fileResource.setFileMd5(fileResourceVO.getFileMd5());
            fileResource.setModule(fileResourceVO.getModule());
            fileResource.setUrl(url);
            fileResource.setUploadStatus(FileResource.UPLOAD_STATUS_1);
            fileResource.setUpdateTime(LocalDateTime.now());
            fileResourceMapper.updateFileResourceByFileMd5AndModule(fileResource);
            return url;
        }
        throw new MyException(ResponseEnums.MINIO_COMPOSE_FILE_ERROR);
    }

    /**
     * 获取文件访问地址
     * @param fileMd5
     * @param module
     */
    @Override
    public String getFileUrl(String fileMd5, Integer module) {
        FileResource fileResource = fileResourceMapper.selectFileResourceByFileMd5AndModule(fileMd5, module);
        if (fileResource == null){
            return null;
        }
        return fileResource.getUrl();
    }

    /**
     * 获取带单位的文件大小
     * @param size
     */
    public String getFileSize(Long size) {
        double num = 1024;

        if (size < num){
            return size + "B";
        }
        if (size < Math.pow(num, 2)){
            return new DecimalFormat("0.00").format(size / num) + "K";
        }
        if (size < Math.pow(num, 3)){
            return new DecimalFormat("0.00").format(size / Math.pow(num, 2)) + "M";
        }
        if (size < Math.pow(num, 4)){
            return new DecimalFormat("0.00").format(size / Math.pow(num, 3)) + "G";
        }
        if (size < Math.pow(num, 5)){
            return new DecimalFormat("0.00").format(size / Math.pow(num, 4)) + "T";
        }
        return null;
    }

    /**
     * 批量删除文件
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Integer[] ids) {
        List<FileResource> fileResourceList = fileResourceMapper.selectFileResourceByIds(ids);

        List<Integer> failList = new ArrayList<>();
        for (FileResource fileResourceListItem : fileResourceList) {
            // 检测文章
            if (articleService.checkByFile(fileResourceListItem.getUrl())){
                failList.add(fileResourceListItem.getId());
                continue;
            }

            // 检测视频
            if (videoService.checkByFile(fileResourceListItem.getUrl())){
                failList.add(fileResourceListItem.getId());
                continue;
            }

            // 检测友链
            if (linkService.checkByFile(fileResourceListItem.getUrl())){
                failList.add(fileResourceListItem.getId());
                continue;
            }

            String[] urls = fileResourceListItem.getUrl().split("/");
            minioUtils.deleteObjectName(fileResourceListItem.getBucketName(),
                    DateTimeFormatter.ofPattern("yyyyMMdd").format(fileResourceListItem.getUpdateTime())
                            + "/" + urls[urls.length - 1]);
            fileResourceMapper.deleteById(fileResourceListItem.getId());
        }

        if (!CollectionUtils.isEmpty(failList)){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "部分文件已有关联，删除失败，列表：" + failList.toString());
        }
    }

}
