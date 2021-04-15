package com.luoyu.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.entity.operation.FriendLink;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.service.operation.FriendLinkService;
import com.luoyu.blog.mapper.operation.FriendLinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
@Service
@Slf4j
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param title
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("title", title);
        IPage<FriendLink> linkPage = baseMapper.selectPage(new Query<FriendLink>(params).getPage(),
                new QueryWrapper<FriendLink>().lambda().like(StringUtils.isNotEmpty(title), FriendLink::getTitle,title));
        return new PageUtils(linkPage);
    }

    /**
     * 判断上传文件下是否有友链
     * @param url
     * @return
     */
    @Override
    public boolean checkByFile(String url) {
        return baseMapper.checkByFile(url) > 0;
    }

    /********************** portal ********************************/

    /**
     * 获取link列表
     *
     * @return
     */
    @Override
    public List<FriendLink> listFriendLink() {
        return baseMapper.selectList(null);
    }

}
