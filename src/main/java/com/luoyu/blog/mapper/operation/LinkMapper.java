package com.luoyu.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.operation.Link;

/**
 * <p>
 * 友链 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
public interface LinkMapper extends BaseMapper<Link> {

    /**
     * 判断上传文件下是否有友链
     * @param url
     * @return
     */
    int checkByFile(String url);

}
