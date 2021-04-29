package com.jinhx.blog.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.entity.file.FileChunk;

import java.util.List;

/**
 * <p>
 * 云存储分片表 服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
public interface FileChunkService extends IService<FileChunk> {

    /**
     * 检查是否上传完所有分片
     *
     * @param fileMd5 fileMd5
     * @return
     */
    Boolean checkIsUploadAllChunkByFileMd5(String fileMd5);

    /**
     * 根据文件md5查询分片信息
     *
     * @param fileMd5 fileMd5
     * @return
     */
    List<FileChunk> selectFileChunksByFileMd5(String fileMd5);

    /**
     * 根据文件md5和分片序号更新状态
     *
     * @param fileChunk fileResource
     * @return
     */
    Boolean updateFileChunkByFileMd5AndChunkNumber(FileChunk fileChunk);

    /**
     * 新增
     *
     * @param fileChunk fileResource
     * @return
     */
    Boolean insertFileChunk(FileChunk fileChunk);

}
