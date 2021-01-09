package com.luoyu.blog.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.entity.file.FileResource;
import com.luoyu.blog.mapper.file.FileResourceMapper;
import com.luoyu.blog.service.file.FileResourceService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 分页查询文件
     * @param page
     * @param limit
     * @param module
     * @return
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

}
