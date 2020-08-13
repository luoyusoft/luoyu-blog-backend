package com.luoyu.blog.project.service.portal.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.operation.Link;

import java.util.List;

/**
 * LinkService
 *
 * @author bobbi
 * @date 2019/02/21 17:09
 * @email 571002217@qq.com
 * @description
 */
public interface LinkService extends IService<Link> {

    /**
     * 获取link列表
     * @return
     */
    List<Link> listLink();
}
