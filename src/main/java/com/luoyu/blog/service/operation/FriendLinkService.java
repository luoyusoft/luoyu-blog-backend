package com.luoyu.blog.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.operation.FriendLink;

import java.util.List;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
public interface FriendLinkService extends IService<FriendLink> {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param title
     * @return
     */
     PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 判断上传文件下是否有友链
     * @param url
     * @return
     */
    boolean checkByFile(String url);

    /********************** portal ********************************/

    /**
     * 获取link列表
     * @return
     */
    List<FriendLink> listFriendLink();

}
