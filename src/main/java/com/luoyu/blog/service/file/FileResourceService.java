package com.luoyu.blog.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.file.FileResource;

/**
 * <p>
 * 云存储资源表 服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
public interface FileResourceService extends IService<FileResource> {

    /**
     * 分页查询文件
     * @param page
     * @param limit
     * @param module
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, Integer module);

}
