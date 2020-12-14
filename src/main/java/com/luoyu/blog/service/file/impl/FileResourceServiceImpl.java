package com.luoyu.blog.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.mapper.file.FileResourceMapper;
import com.luoyu.blog.service.file.FileResourceService;
import org.springframework.stereotype.Service;

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

}
