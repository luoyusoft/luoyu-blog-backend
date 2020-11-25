package com.luoyu.blogmanage.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.entity.operation.Link;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
public interface LinkService extends IService<Link> {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param title
     * @return
     */
     PageUtils queryPage(Integer page, Integer limit, String title);

}
