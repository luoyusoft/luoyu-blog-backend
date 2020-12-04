package com.luoyu.blog.service.oss.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.entity.oss.OssResource;
import com.luoyu.blog.mapper.oss.OssResourceMapper;
import com.luoyu.blog.service.oss.OssResourceService;
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
public class OssResourceServiceImpl extends ServiceImpl<OssResourceMapper, OssResource> implements OssResourceService {

}
