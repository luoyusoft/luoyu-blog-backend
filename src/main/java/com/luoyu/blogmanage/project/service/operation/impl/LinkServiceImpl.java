package com.luoyu.blogmanage.project.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.common.entity.operation.Link;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.util.Query;
import com.luoyu.blogmanage.project.service.operation.LinkService;
import com.luoyu.blogmanage.project.mapper.operation.LinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author bobbi
 * @since 2019-02-14
 */
@Service
@Slf4j
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    /**
     * 分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        IPage<Link> page=baseMapper.selectPage(new Query<Link>(params).getPage(),
                new QueryWrapper<Link>().lambda().like(StringUtils.isNotEmpty(title),Link::getTitle,title));
        return new PageUtils(page);
    }

}
