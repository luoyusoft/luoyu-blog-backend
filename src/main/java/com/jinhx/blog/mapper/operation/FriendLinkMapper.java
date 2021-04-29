package com.jinhx.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinhx.blog.entity.operation.FriendLink;

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
     * @return 上传文件下友链数量
     */
    Integer checkByFile(String url);

    /**
     * 获取总量
     * @return 总量
     */
    Integer selectCount();

}
