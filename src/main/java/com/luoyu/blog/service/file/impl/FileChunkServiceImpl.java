package com.luoyu.blog.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.entity.file.FileChunk;
import com.luoyu.blog.mapper.file.FileChunkMapper;
import com.luoyu.blog.service.file.FileChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 云存储分片表 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Service
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunk> implements FileChunkService {

    @Autowired
    private FileChunkMapper fileChunkMapper;

    @Override
    public Boolean checkIsUploadAllChunkByFileMd5(String fileMd5) {
        return fileChunkMapper.checkIsUploadAllChunkByFileMd5(fileMd5, FileChunk.UPLOAD_STATUS_0) < 1;
    }

    @Override
    public List<FileChunk> selectFileChunksByFileMd5(String fileMd5) {
        return fileChunkMapper.selectFileChunksByFileMd5(fileMd5);
    }

    @Override
    public Boolean updateFileChunkByFileMd5AndChunkNumber(FileChunk fileChunk) {
        return fileChunkMapper.updateFileChunkByFileMd5AndChunkNumber(fileChunk);
    }

    @Override
    public Boolean insertFileChunk(FileChunk fileChunk) {
        return fileChunkMapper.insertFileChunk(fileChunk);
    }

}
