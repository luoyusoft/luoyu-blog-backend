package com.luoyu.blog.project.service.portal.operation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.entity.operation.Link;
import com.luoyu.blog.project.service.portal.operation.LinkService;
import com.luoyu.blog.project.mapper.manage.operation.LinkMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LinkService
 *
 * @author bobbi
 * @date 2019/02/21 17:10
 * @email 571002217@qq.com
 * @description
 */
@Service("linkPortalService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {


    /**
     * 获取link列表
     *
     * @return
     */
    @Override
    public List<Link> listLink() {
        return baseMapper.selectList(null);
    }
}
