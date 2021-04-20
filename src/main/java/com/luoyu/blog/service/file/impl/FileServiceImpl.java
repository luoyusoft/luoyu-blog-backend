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
import com.luoyu.blog.entity.file.File;
import com.luoyu.blog.entity.file.vo.FileVO;
import com.luoyu.blog.mapper.file.FileMapper;
import com.luoyu.blog.service.article.ArticleService;
import com.luoyu.blog.service.file.FileChunkService;
import com.luoyu.blog.service.file.FileService;
import com.luoyu.blog.service.operation.FriendLinkService;
import com.luoyu.blog.service.video.VideoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private MinioUtils minioUtils;

    @Resource
    private FileMapper fileMapper;

    @Autowired
    private FileChunkService fileChunkService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private FriendLinkService friendLinkService;

    @Value("${minio.base.url}")
    private String minioBaseUrl;

    /**
     * 上传
     * @param file
     * @param fileModule
     */
    @Override
    public FileVO upload(MultipartFile file, Integer fileModule) {
        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            String contentType = file.getContentType();
            String patchName = getPath() + suffix;
            String storageType = File.STORAGE_TYPE_MINIO;
            String bucketName;
            if (suffix.equals(".mp4")){
                bucketName = File.BUCKET_NAME_VIDEO;
            }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
                bucketName = File.BUCKET_NAME_IMG;
            }else {
                bucketName = File.BUCKET_NAME_OTHER;
//                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
            }

            File fileResource = new File();
            fileResource.setModule(fileModule);

            minioUtils.upload(inputStream, patchName, bucketName, contentType);
            String url = minioUtils.getObjectUrl(bucketName, patchName);
            url = url.replace(minioBaseUrl, "https://minio.luoyublog.com").substring(0, url.indexOf("?") + 1);
            fileResource.setFileName(fileName);
            fileResource.setBucketName(bucketName);
            fileResource.setStorageType(storageType);
            fileResource.setUrl(url);
            fileResource.setIsChunk(File.IS_CHUNK_0);
            fileResource.setChunkCount(0);
            fileResource.setUploadStatus(File.UPLOAD_STATUS_1);
            fileResource.setSuffix(suffix);
            fileResource.setFileMd5(DigestUtils.md5Hex(file.getInputStream()));
            fileResource.setFileSize(getFileSize(file.getSize()));
            baseMapper.insert(fileResource);
            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setUrl(url);
            return fileVO;
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
            bucketName = File.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = File.BUCKET_NAME_IMG;
        }else {
            bucketName = File.BUCKET_NAME_OTHER;
//            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "不存在该文件类型");
        }

        minioUtils.download(response, bucketName, fileName);
    }

    /**
     * 分页查询文件
     * @param page page
     * @param limit limit
     * @param module module
     * @param fileName fileName
     * @param fileMd5 fileMd5
     * @param url url
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, Integer module, String fileName, String fileMd5, String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));

        IPage<File> fileResourceIPage = baseMapper.selectPage(new Query<File>(params).getPage(),
                new QueryWrapper<File>().lambda()
                        .eq(module != null, File::getModule, module)
                        .like(!StringUtils.isEmpty(fileName), File::getFileName, fileName)
                        .like(!StringUtils.isEmpty(fileMd5), File::getFileMd5, fileMd5)
                        .like(!StringUtils.isEmpty(url), File::getUrl, url)
                        .orderByDesc(File::getCreateTime)
        );
        return new PageUtils(fileResourceIPage);
    }

    /**
     * 分片上传文件
     * @param fileVO
     */
    @Override
    public List<FileVO> chunk(FileVO fileVO) {
        String bucketName = null;
        File fileResource = fileMapper.selectFileByFileMd5AndModule(fileVO.getFileMd5(), fileVO.getModule());
        // 校验该文件是否上传过
        if(fileResource != null){
            // 秒传
            if(fileResource.getUploadStatus().equals(File.UPLOAD_STATUS_1)){
                return Collections.emptyList();
            }
            // 续传
            List<FileChunk> fileChunks = fileChunkService.selectFileChunksByFileMd5(fileResource.getFileMd5());
            if (!CollectionUtils.isEmpty(fileChunks)){
                List<FileVO> fileVOList = new ArrayList<>();
                for (FileChunk fileChunk : fileChunks){
                    FileVO file = new FileVO();
                    file.setUploadUrl(fileChunk.getUploadUrl());
                    file.setChunkNumber(fileChunk.getChunkNumber());
                    file.setUploadStatus(fileChunk.getUploadStatus());
                    file.setFileMd5(fileChunk.getFileMd5());
                    file.setBucketName(bucketName);
                    fileVOList.add(file);
                }

                return fileVOList;
            }
        }
        // 初次上传
        String suffix = fileVO.getFileName().substring(fileVO.getFileName().lastIndexOf("."));
        if (suffix.equals(".mp4")){
            bucketName = File.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = File.BUCKET_NAME_IMG;
        }else {
            bucketName = File.BUCKET_NAME_OTHER;
//            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
        }
        List<String> uploadUrls = minioUtils.createUploadChunkUrlList(bucketName, fileVO.getFileMd5(), fileVO.getChunkCount(), 604800);
        List<FileVO> fileVOList = new ArrayList<>();
        for (int i = 0; i < uploadUrls.size(); i++) {
            FileVO file = new FileVO();
            String url = minioUtils.createUploadChunkUrl(bucketName, fileVO.getFileMd5(), i, 604800);
            file.setUploadUrl(url);
            file.setChunkNumber(i);
            file.setUploadStatus(FileChunk.UPLOAD_STATUS_0);
            file.setFileMd5(fileVO.getFileMd5());
            file.setBucketName(bucketName);
            fileVOList.add(file);

            FileChunk fileChunk = new FileChunk();
            fileChunk.setFileMd5(fileVO.getFileMd5());
            fileChunk.setUploadUrl(url);
            fileChunk.setUploadStatus(FileChunk.UPLOAD_STATUS_0);
            fileChunk.setChunkNumber(file.getChunkNumber());
            fileChunkService.insertFileChunk(fileChunk);
        }
        // 向数据库中记录该文件的上传信息
        File newFile = new File();
        newFile.setFileName(fileVO.getFileName());
        newFile.setFileMd5(fileVO.getFileMd5());
        newFile.setBucketName(bucketName);
        newFile.setFileSize(getFileSize(Long.valueOf(fileVO.getFileSize())));
        newFile.setIsChunk(File.IS_CHUNK_1);
        newFile.setStorageType(File.STORAGE_TYPE_MINIO);
        newFile.setModule(fileVO.getModule());
        newFile.setSuffix(suffix);
        newFile.setChunkCount(fileVO.getChunkCount());
        newFile.setUploadStatus(File.UPLOAD_STATUS_0);
        baseMapper.insert(newFile);

        return fileVOList;
    }

    /**
     * 分片上传，单个分片成功
     * @param fileVO
     */
    @Override
    public Boolean chunkUploadSuccess(FileVO fileVO) {
        File file = fileMapper.selectFileByFileMd5AndModule(fileVO.getFileMd5(), fileVO.getModule());
        if (file == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该文件未上传过");
        }

        FileChunk fileChunk = new FileChunk();
        fileChunk.setFileMd5(fileVO.getFileMd5());
        fileChunk.setUploadStatus(FileChunk.UPLOAD_STATUS_1);
        fileChunk.setChunkNumber(fileVO.getChunkNumber());
        fileChunk.setUpdateTime(LocalDateTime.now());
        return fileChunkService.updateFileChunkByFileMd5AndChunkNumber(fileChunk);
    }

    /**
     * 合并文件并返回文件信息
     * @param fileVO
     */
    @Override
    public String composeFile(FileVO fileVO) {
        if (fileMapper.selectFileByFileMd5AndModule(fileVO.getFileMd5(), fileVO.getModule()) == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该文件未上传过");
        }
        if(!fileChunkService.checkIsUploadAllChunkByFileMd5(fileVO.getFileMd5())){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该文件还有部分分片未上传");
        }

        String bucketName;
        String suffix = fileVO.getFileName().substring(fileVO.getFileName().lastIndexOf("."));
        if (suffix.equals(".mp4")){
            bucketName = File.BUCKET_NAME_VIDEO;
        }else if (suffix.equals(".gif") || suffix.equals(".jpg") || suffix.equals(".png")){
            bucketName = File.BUCKET_NAME_IMG;
        }else {
            bucketName = File.BUCKET_NAME_OTHER;
//            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "暂不支持该文件格式");
        }
        // 根据md5获取所有分片文件名称(minio的文件名称 = 文件path)
        List<String> chunks = minioUtils.listObjectNames(bucketName, fileVO.getFileMd5(), true);

        // 自定义文件名称
        String patchName = getPath() + suffix;

        // 合并文件
        if(minioUtils.composeObject(bucketName, bucketName, chunks, patchName)){
            minioUtils.deleteObjectNames(bucketName, chunks);

            // 获取文件访问外链(1小时过期)
            String url = minioUtils.getObjectUrl(bucketName, patchName);
            url = url.replace(minioBaseUrl, "https://minio.luoyublog.com").substring(0, url.indexOf("?") + 1);
            // 获取数据库里记录的文件信息，修改数据并返回文件信息
            File file = new File();
            file.setFileMd5(fileVO.getFileMd5());
            file.setModule(fileVO.getModule());
            file.setUrl(url);
            file.setUploadStatus(File.UPLOAD_STATUS_1);
            file.setUpdateTime(LocalDateTime.now());
            fileMapper.updateFileByFileMd5AndModule(file);
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
        File file = fileMapper.selectFileByFileMd5AndModule(fileMd5, module);
        if (file == null){
            return null;
        }
        return file.getUrl();
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
        List<File> fileList = fileMapper.selectFileByIds(ids);

        List<Integer> failList = new ArrayList<>();
        for (File fileListItem : fileList) {
            // 检测文章
            if (articleService.checkByFile(fileListItem.getUrl())){
                failList.add(fileListItem.getId());
                continue;
            }

            // 检测视频
            if (videoService.checkByFile(fileListItem.getUrl())){
                failList.add(fileListItem.getId());
                continue;
            }

            // 检测友链
            if (friendLinkService.checkByFile(fileListItem.getUrl())){
                failList.add(fileListItem.getId());
                continue;
            }

            String[] urls = fileListItem.getUrl().split("/");
            minioUtils.deleteObjectName(fileListItem.getBucketName(),
                    DateTimeFormatter.ofPattern("yyyyMMdd").format(fileListItem.getUpdateTime())
                            + "/" + urls[urls.length - 1]);
            fileMapper.deleteById(fileListItem.getId());
        }

        if (!CollectionUtils.isEmpty(failList)){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "部分文件已有关联，删除失败，列表：" + failList.toString());
        }
    }

}
