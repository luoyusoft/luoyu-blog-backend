package com.luoyu.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.operation.FriendLink;

/**
 * <p>
 * 友链 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

    /**
     * 判断上传文件下是否有友链
     * @param url
     * @return
     */
    int checkByFile(String url);

}
